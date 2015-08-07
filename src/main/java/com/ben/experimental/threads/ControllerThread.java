package com.ben.experimental.threads;

import com.ben.experimental.components.controller.Controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ben on 8/4/2015.
 */
public class ControllerThread extends AbstractThread {

    private static final Logger LOG = LogManager.getLogger(ControllerThread.class);

    public ControllerThread() {
        super(new Controller());
    }
}
