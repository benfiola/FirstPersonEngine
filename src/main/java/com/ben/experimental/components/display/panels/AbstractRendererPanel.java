package com.ben.experimental.components.display.panels;

import com.ben.experimental.components.display.calculators.AbstractGraphicCalculator;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.events.DrawEvent;
import com.ben.experimental.events.InputEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.utils.ObjectCloner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ben on 8/5/2015.
 */
public abstract class AbstractRendererPanel extends AbstractPanel {
    private Logger LOG = LogManager.getLogger(this.getClass());

    protected List<AbstractGraphicData> toDraw;

    public AbstractRendererPanel() {
        super();
        toDraw = new LinkedList<AbstractGraphicData>();
    }

    public void receiveDrawEvent(DrawEvent event) {
        DrawEvent clone = (DrawEvent) ObjectCloner.clone(event);
        processDrawEvent(clone, this.getSize());
    }

    protected void processDrawEvent(DrawEvent event, Dimension windowSize) {
        toDraw = getGraphicsCalculator().calculate(event.getPlayer(), event.getMap(), windowSize);
    }

    protected abstract AbstractGraphicCalculator getGraphicsCalculator();
    protected abstract void paintComponent(Graphics g);

    @Override
    public void keyPressed(KeyEvent e) {
        EventDispatcher.dispatch(new InputEvent(e.getKeyCode()));
    }
}
