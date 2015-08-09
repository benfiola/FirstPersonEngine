package com.ben.experimental.components.poller;

import com.ben.experimental.events.InitializeEvent;
import com.ben.experimental.events.PollEvent;
import com.ben.experimental.events.RequestUpdateEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.events.interfaces.PollerEvent;
import com.ben.experimental.main.AbstractComponent;

import java.util.Date;

/**
 * Created by Ben on 8/9/2015.
 */
public class UpdatePoller extends AbstractComponent<PollerEvent> {
    private static final long POLL_INTERVAL = 5;
    private long lastPoll;

    public UpdatePoller() {

    }

    @Override
    public void handleEvent(PollerEvent componentEvent) {
        if(componentEvent instanceof InitializeEvent) {
            initialize();
        }
        if(componentEvent instanceof PollEvent) {
            long currTime = new Date().getTime();
            if((currTime - lastPoll >= POLL_INTERVAL)) {
                EventDispatcher.dispatch(new RequestUpdateEvent());
                lastPoll = currTime;
            }
            EventDispatcher.dispatch(new PollEvent());
        }
    }

    public void initialize() {
        EventDispatcher.dispatch(new PollEvent());
    }


    @Override
    public Class<? extends ComponentEvent> getEventInterface() {
        return PollerEvent.class;
    }
}
