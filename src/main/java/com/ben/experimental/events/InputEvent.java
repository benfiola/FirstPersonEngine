package com.ben.experimental.events;

import com.ben.experimental.events.interfaces.ControllerEvent;

/**
 * Created by Ben on 8/6/2015.
 */
public class InputEvent implements ControllerEvent {
    private int keyCode;

    public InputEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    public int getKeyCode() {
        return this.keyCode;
    }
}
