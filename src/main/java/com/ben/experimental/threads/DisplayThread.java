package com.ben.experimental.threads;

import com.ben.experimental.components.display.Display;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ben on 8/4/2015.
 */
public class DisplayThread extends AbstractThread {
    private static final Logger LOG = LogManager.getLogger(DisplayThread.class);

    public DisplayThread() {
        super(new Display());
    }
}
