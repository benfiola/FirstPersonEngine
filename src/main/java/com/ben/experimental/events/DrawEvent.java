package com.ben.experimental.events;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.events.interfaces.DisplayEvent;

/**
 * Created by Ben on 8/5/2015.
 */
public class DrawEvent extends AbstractEvent implements DisplayEvent {
    private Player player;
    private Map map;

    public DrawEvent(Player player, Map map) {
        this.player = player;
        this.map = map;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Map getMap() {
        return this.map;
    }
}
