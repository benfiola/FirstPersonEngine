package com.ben.experimental.components.controller;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.events.DrawEvent;
import com.ben.experimental.events.InputEvent;
import com.ben.experimental.events.dispatcher.EventDispatcher;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.Box;
import com.ben.experimental.events.interfaces.ControllerEvent;
import com.ben.experimental.events.interfaces.ComponentEvent;
import com.ben.experimental.main.AbstractComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 8/5/2015.
 */
public class Controller extends AbstractComponent<ControllerEvent> {
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    private Map map;
    private Player player;

    public Controller() {
        super();
        map = createMap();
        player = new Player(new SerializablePoint3D(0.0,0.0,0.0), 0.0);
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

    public static final Map createMap() {
        List<AbstractGeometry> geometries = new ArrayList<AbstractGeometry>();
        Box b1 = new Box(new SerializablePoint3D(0.0,0.0,0.0),10.0, 10.0, 10.0);
        geometries.add(b1);
        return new Map(geometries);
    }

    public void handleEvent(ControllerEvent event) {
        if(event instanceof InputEvent) {
            handleInputEvent((InputEvent) event);
        }
    }

    private void handleInputEvent(InputEvent event) {
        //38 - up
        //37 - left
        //40 - down
        //39 - right
        sendUpdate();
    }

    private void sendUpdate() {
        EventDispatcher.dispatch(new DrawEvent(player, map));
    }
}
