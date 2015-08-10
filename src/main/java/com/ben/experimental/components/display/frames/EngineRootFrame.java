package com.ben.experimental.components.display.frames;

import com.ben.experimental.components.display.panels.AbstractRendererPanel;
import com.ben.experimental.components.display.panels.TwoDimensionalRendererPanel;
import com.ben.experimental.events.ExitEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * Created by Ben on 8/4/2015.
 */
public class EngineRootFrame extends AbstractRootFrame {

    private AbstractRendererPanel currentPanel;

    public EngineRootFrame() {
        super("Test");
        setSize(800, 800);
        setRendererPanel(new TwoDimensionalRendererPanel());
        setVisible(true);
    }

    public AbstractRendererPanel getRendererPanel() {
        return this.currentPanel;
    }

    public void setRendererPanel(AbstractRendererPanel panel) {
        if(currentPanel != null) {
            currentPanel.cleanUp();
            getContentPane().remove(currentPanel);
        }
        currentPanel = panel;
        getContentPane().add(currentPanel);
        currentPanel.initialize();
        revalidate();
    }

    @Override
    public void windowClosing(WindowEvent e) {
        EventDispatcher.dispatch(new ExitEvent());
    }
}
