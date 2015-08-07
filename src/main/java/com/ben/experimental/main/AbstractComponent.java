package com.ben.experimental.main;

import com.ben.experimental.events.interfaces.ComponentEvent;

/**
 * Created by Ben on 8/6/2015.
 */
public abstract class AbstractComponent<T extends ComponentEvent> {

    public AbstractComponent() {
    }

    public abstract void handleEvent(T componentEvent);
    public abstract Class<? extends ComponentEvent> getEventInterface();
}
