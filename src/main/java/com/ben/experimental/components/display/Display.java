package com.ben.experimental.components.display;

import com.ben.experimental.components.display.frames.EngineRootFrame;
import com.ben.experimental.components.display.panels.ThreeDimensionalRendererPanel;
import com.ben.experimental.components.display.panels.TwoDimensionalRendererPanel;
import com.ben.experimental.events.DrawEvent;
import com.ben.experimental.events.InitializeEvent;
import com.ben.experimental.events.SwitchRendererEvent;
import com.ben.experimental.events.ZoomEvent;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.events.interfaces.DisplayEvent;
import com.ben.experimental.main.AbstractComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Ben on 8/5/2015.
 */
public class Display extends AbstractComponent<DisplayEvent> {
    private static final Logger LOG = LogManager.getLogger(Display.class);

    private EngineRootFrame rootFrame;
    private int graphicMode;

    public Display() {
        super();
    }

    @Override
    public void handleEvent(DisplayEvent displayEvent) {
        if(displayEvent instanceof InitializeEvent) {
            initialize();
        }
        if(displayEvent instanceof SwitchRendererEvent) {
            handleSwitchRendererEvent((SwitchRendererEvent) displayEvent);
        }
        if(displayEvent instanceof DrawEvent) {
            handleDrawEvent((DrawEvent) displayEvent);
        }
        if(displayEvent instanceof ZoomEvent) {
            handleZoomEvent((ZoomEvent) displayEvent);
        }
    }

    private void initialize() {
        rootFrame = new EngineRootFrame();
    }

    private void handleSwitchRendererEvent(SwitchRendererEvent event) {
        if (graphicMode == 1) {
            LOG.info("Switching to " + TwoDimensionalRendererPanel.class.getSimpleName());
            graphicMode = 0;
            rootFrame.setRendererPanel(new TwoDimensionalRendererPanel());
        } else {
            graphicMode = 1;
            LOG.info("Switching to " + ThreeDimensionalRendererPanel.class.getSimpleName());
            rootFrame.setRendererPanel(new ThreeDimensionalRendererPanel());
        }
    }

    private void handleDrawEvent(DrawEvent event) {
        rootFrame.getRendererPanel().receiveDrawEvent(event);
    }

    private void handleZoomEvent(ZoomEvent event) {
        rootFrame.getRendererPanel().receiveZoomEvent(event);
    }

    @Override
    public Class<? extends ComponentEvent> getEventInterface() {
        return DisplayEvent.class;
    }
}
