package com.ben.experimental.components.display.panels;

import com.ben.experimental.components.display.calculators.AbstractGraphicCalculator;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.events.*;
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
    private static final Double ZOOM_FACTOR_MODIFIER = 0.5;

    protected List<AbstractGraphicData> toDraw;
    protected AbstractGraphicCalculator calculator;
    protected Double zoomFactor;

    public AbstractRendererPanel(AbstractGraphicCalculator calculator) {
        super();
        this.calculator = calculator;
        this.zoomFactor = 10.0;
        toDraw = new LinkedList<AbstractGraphicData>();
    }

    public void receiveDrawEvent(DrawEvent event) {
        DrawEvent clone = (DrawEvent) ObjectCloner.clone(event);
        processDrawEvent(clone, this.getSize());
    }

    public void receiveZoomEvent(ZoomEvent event) {
        if(event.getZoomType() == ZoomEvent.ZoomType.IN) {
            zoomFactor = zoomFactor + ZOOM_FACTOR_MODIFIER;
        } else if(event.getZoomType() == ZoomEvent.ZoomType.OUT) {
            if(zoomFactor > ZOOM_FACTOR_MODIFIER) {
                zoomFactor = zoomFactor - ZOOM_FACTOR_MODIFIER;
            }
        }
       EventDispatcher.dispatch(new RequestUpdateEvent());
    }

    protected void processDrawEvent(DrawEvent event, Dimension windowSize) {
        toDraw = calculator.calculate(event.getPlayer(), event.getMap(), windowSize, zoomFactor);
        repaint();
    }

    protected abstract void paintComponent(Graphics g);

    private void handleKeyEvent(KeyEvent e, boolean enabled) {
        switch(e.getKeyCode()) {
            //-
            case 45 :
                EventDispatcher.dispatch(new ZoomEvent(ZoomEvent.ZoomType.OUT));
                break;
            //+
            case 61 :
                EventDispatcher.dispatch(new ZoomEvent(ZoomEvent.ZoomType.IN));
                break;
            //up
            case 38 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.FORWARD, enabled));
                break;
            //down
            case 40 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.BACKWARD, enabled));
                break;
            //left
            case 37 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.LEFT, enabled));
                break;
            //right
            case 39 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.RIGHT, enabled));
                break;
            default :
                LOG.info(e.getKeyCode());
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            //-
            case 45 :
                EventDispatcher.dispatch(new ZoomEvent(ZoomEvent.ZoomType.OUT));
                break;
            //+
            case 61 :
                EventDispatcher.dispatch(new ZoomEvent(ZoomEvent.ZoomType.IN));
                break;
            //up
            case 38 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.FORWARD, true));
                break;
            //down
            case 40 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.BACKWARD, true));
                break;
            //left
            case 37 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.LEFT, true));
                break;
            //right
            case 39 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.RIGHT, true));
                break;
            default :
                LOG.info(e.getKeyCode());
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            //up
            case 38 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.FORWARD, false));
                break;
            //down
            case 40 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.BACKWARD, false));
                break;
            //left
            case 37 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.LEFT, false));
                break;
            //right
            case 39 :
                EventDispatcher.dispatch(new MovementEvent(MovementEvent.MovementType.RIGHT, false));
                break;
            default :
                break;
        }
    }
}
