package com.ben.experimental.components.display.frames;

import com.ben.experimental.components.display.panels.AbstractRendererPanel;
import com.ben.experimental.components.display.panels.TwoDimensionalRendererPanel;
import com.ben.experimental.events.ExitEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;

import java.awt.event.WindowEvent;

/**
 * Created by Ben on 8/4/2015.
 */
public class EngineRootFrame extends AbstractRootFrame {

    private AbstractRendererPanel currentPanel;

    public EngineRootFrame() {
        super("Test");
        setSize(800, 800);
        currentPanel = new TwoDimensionalRendererPanel();
        add(currentPanel);
        setVisible(true);
    }

    public AbstractRendererPanel getRendererPanel() {
        return this.currentPanel;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        EventDispatcher.dispatch(new ExitEvent());
    }
}
