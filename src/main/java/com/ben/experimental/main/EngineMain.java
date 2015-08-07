package com.ben.experimental.main;

import com.ben.experimental.threads.ControllerThread;
import com.ben.experimental.threads.DisplayThread;
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
            displayThread.start();
            controllerThread.start();
            controllerThread.join();
            displayThread.join();
        } catch(Exception e) {
            LOG.error("Error : ", e);
        }
        LOG.info("Exiting");
        System.exit(0);
    }
}
