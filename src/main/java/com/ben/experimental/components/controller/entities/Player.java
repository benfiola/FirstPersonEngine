package com.ben.experimental.components.controller.entities;

import com.ben.experimental.components.controller.geometry.SerializablePoint3D;

import java.io.Serializable;

/**
 * Created by Ben on 8/5/2015.
 */
public class Player implements Serializable {
    private SerializablePoint3D location;

    public Player(SerializablePoint3D location) {
        this.location = location;
    }

    public SerializablePoint3D getLocation() {
        return this.location;
    }

    public void setLocation(SerializablePoint3D location) {
        this.location = location;
    }
}
