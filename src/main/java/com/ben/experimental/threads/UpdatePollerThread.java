package com.ben.experimental.threads;

import com.ben.experimental.components.poller.UpdatePoller;
import com.ben.experimental.main.AbstractComponent;

/**
 * Created by Ben on 8/9/2015.
 */
public class UpdatePollerThread extends AbstractThread {

    public UpdatePollerThread() {
        super(new UpdatePoller());
    }
}
