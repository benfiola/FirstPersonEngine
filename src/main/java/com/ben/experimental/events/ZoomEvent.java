package com.ben.experimental.events;

import com.ben.experimental.events.interfaces.DisplayEvent;

/**
 * Created by Ben on 8/9/2015.
 */
public class ZoomEvent implements DisplayEvent {
    public enum ZoomType {
        IN, OUT
    };

    private ZoomType type;

    public ZoomEvent(ZoomType type) {
        this.type = type;
    }

    public ZoomType getZoomType() {
        return this.type;
    }
}
