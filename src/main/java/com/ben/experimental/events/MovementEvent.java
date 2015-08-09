package com.ben.experimental.events;

import com.ben.experimental.events.interfaces.ControllerEvent;

/**
 * Created by Ben on 8/9/2015.
 */
public class MovementEvent implements ControllerEvent {
    public enum MovementType {
        FORWARD,
        BACKWARD,
        LEFT,
        RIGHT
    }

    private boolean enabled;
    private MovementType type;

    public MovementEvent(MovementType type, boolean enabled) {
        this.type = type;
        this.enabled = enabled;
    }

    public MovementType getType() {
        return this.type;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
