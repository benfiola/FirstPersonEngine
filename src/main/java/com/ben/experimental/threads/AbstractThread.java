package com.ben.experimental.threads;

import com.ben.experimental.events.ExitEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.main.AbstractComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by Ben on 8/4/2015.
 */
public abstract class AbstractThread extends Thread {
    private final Logger LOG = LogManager.getLogger(this.getClass());
    private ArrayBlockingQueue<ComponentEvent> queue = new ArrayBlockingQueue<ComponentEvent>(100);
    protected AbstractComponent component;
    private boolean shouldExit = false;

    public void receiveEvent(ComponentEvent componentEvent) {
        queue.offer(componentEvent);
    }

    public AbstractThread(AbstractComponent component) {
        this.component = component;
        EventDispatcher.registerThread(this);
    }

    public Class<? extends ComponentEvent> getComponentEventInterface() {
        return this.component.getEventInterface();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void run() {
        LOG.info("Starting");
        while (!shouldExit) {
            if (!queue.isEmpty()) {
                ComponentEvent componentEvent = queue.poll();
                if(componentEvent instanceof ExitEvent) {
                    shouldExit = true;
                } else {
                    component.handleEvent(componentEvent);
                }
            }
        }
    }
}
