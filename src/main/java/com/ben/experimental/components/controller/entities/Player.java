package com.ben.experimental.components.controller.entities;

import com.ben.experimental.components.controller.geometry.SerializablePoint3D;

import java.io.Serializable;

/**
 * Created by Ben on 8/5/2015.
 */
public class Player implements Serializable {
    private SerializablePoint3D location;
    private double directionRadians;

    public Player(SerializablePoint3D location, double directionRadians) {
        this.location = location;
        this.directionRadians = directionRadians;
    }

    public SerializablePoint3D getLocation() {
        return this.location;
    }

    public void setLocation(SerializablePoint3D location) {
        this.location = location;
    }

    public double getDirectionRadians() {
        return this.directionRadians;
    }

    public void setDirectionRadians(double directionRadians) {
        this.directionRadians = directionRadians;
    }
}
