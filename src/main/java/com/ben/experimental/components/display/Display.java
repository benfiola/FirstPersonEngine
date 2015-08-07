package com.ben.experimental.components.display;

import com.ben.experimental.components.display.frames.EngineRootFrame;
import com.ben.experimental.events.DrawEvent;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.events.interfaces.DisplayEvent;
import com.ben.experimental.main.AbstractComponent;

/**
 * Created by Ben on 8/5/2015.
 */
public class Display extends AbstractComponent<DisplayEvent> {
    private EngineRootFrame rootFrame;

    public Display() {
        super();
        rootFrame = new EngineRootFrame();
    }

    @Override
    public void handleEvent(DisplayEvent displayEvent) {
        if(displayEvent instanceof DrawEvent) {
            handleDrawEvent((DrawEvent) displayEvent);
        }
    }

    private void handleDrawEvent(DrawEvent event) {
        rootFrame.getRendererPanel().receiveDrawEvent(event);
    }

    @Override
    public Class<? extends ComponentEvent> getEventInterface() {
        return DisplayEvent.class;
    }


}
