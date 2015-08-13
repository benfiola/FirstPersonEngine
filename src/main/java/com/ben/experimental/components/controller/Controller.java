package com.ben.experimental.components.controller;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.*;
import com.ben.experimental.events.*;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.events.interfaces.ControllerEvent;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.main.AbstractComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ben on 8/5/2015.
 */
public class Controller extends AbstractComponent<ControllerEvent> {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    private Map map;
    private Player player;
    private long lastUpdate;

    public Controller() {
        super();
    }

    @Override
    public Class<? extends ComponentEvent> getEventInterface() {
        return ControllerEvent.class;
    }

    public Map getMap() {
        return this.map;
    }

    public Player getPlayer() {
        return this.player;
    }

    public static final Map createBox() {
        List<AbstractGeometry> geometries = new ArrayList<AbstractGeometry>();
        Box b1 = new Box(new SerializablePoint3D(-2.0,-2.0,-2.0),2.0, 2.0, 2.0);
        geometries.add(b1);
        return new Map(geometries);
    }

    public static final Map createLines() {
        List<AbstractGeometry> geometries = new ArrayList<AbstractGeometry>();
        Line l1 = new Line(new SerializablePoint3D(0.0, 0.0, 0.0), new SerializablePoint3D(10000.0, 3.0, 0.0));
        geometries.add(l1);
        return new Map(geometries);
    }

    public void handleEvent(ControllerEvent event) {
        if(event instanceof InitializeEvent) {
            initialize();
        }
        if(event instanceof RequestUpdateEvent) {
            sendUpdate();
        }
        if(event instanceof MovementEvent) {
            handleMovementEvent((MovementEvent) event);
        }
    }

    public void initialize() {
        map = createLines();
        player = new Player(new SerializablePoint3D(0.0,0.0,0.0), 0.0);
    }

    private void handleMovementEvent(MovementEvent event) {
        player.handleMovementEvent(event);
    }

    private void sendUpdate() {
        long temp = lastUpdate;
        lastUpdate = new Date().getTime();
        long duration = lastUpdate - temp;
        player.updateLocation(duration);
        EventDispatcher.dispatch(new DrawEvent(player, map));
    }
}
