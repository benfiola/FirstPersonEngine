package com.ben.experimental.main;

import com.ben.experimental.events.InitializeEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.threads.ControllerThread;
import com.ben.experimental.threads.DisplayThread;
import com.ben.experimental.threads.UpdatePollerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ben on 8/4/2015.
 */
public class EngineMain {
    private static final Logger LOG = LogManager.getLogger(EngineMain.class);

    public static void main(String[] args) {
        LOG.info("Starting");
        try {
            DisplayThread displayThread = new DisplayThread();
            ControllerThread controllerThread = new ControllerThread();
            UpdatePollerThread updatePoller = new UpdatePollerThread();
            displayThread.start();
            controllerThread.start();
            updatePoller.start();
            EventDispatcher.dispatch(new InitializeEvent());
            controllerThread.join();
            updatePoller.join();
            displayThread.join();
        } catch(Exception e) {
            LOG.error("Error : ", e);
        }
        LOG.info("Exiting");
        System.exit(0);
    }
}
