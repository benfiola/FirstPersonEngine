package com.ben.experimental.components.controller.entities;


import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.events.MovementEvent;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Ben on 8/5/2015.
 */
public class Player implements Serializable {
    private static final double moveSpeedPerSecond = 10.0;
    private static final double rotateSpeedPerSecond = 6.0;
    private java.util.Map<MovementEvent.MovementType, Boolean> movement;

    private SerializablePoint3D location;
    private double directionRadians;

    public Player(SerializablePoint3D location, double directionRadians) {
        this.location = location;
        this.directionRadians = directionRadians;
        this.movement = new HashMap<MovementEvent.MovementType, Boolean>();
        for(MovementEvent.MovementType type : MovementEvent.MovementType.values()) {
            movement.put(type, false);
        }
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

    private void moveForward(double durationMs) {
        double movementAmount = moveSpeedPerSecond * (durationMs/1000);
        double xAmount = Math.cos(directionRadians) * movementAmount;
        double yAmount = Math.sin(directionRadians) * movementAmount;
        double newX = location.getX() + xAmount;
        double newY = location.getY() + yAmount;
        double newZ = location.getZ();
        setLocation(new SerializablePoint3D(newX, newY, newZ));
    }

    private void moveBackward(double durationMs) {
        double movementAmount = -moveSpeedPerSecond * (durationMs/1000);
        double xAmount = Math.cos(directionRadians) * movementAmount;
        double yAmount = Math.sin(directionRadians) * movementAmount;
        double newX = location.getX() + xAmount;
        double newY = location.getY() + yAmount;
        double newZ = location.getZ();
        setLocation(new SerializablePoint3D(newX, newY, newZ));
    }

    private void rotateLeft(double durationMs) {
        directionRadians += rotateSpeedPerSecond * (durationMs/1000);
        if(directionRadians > 2 * Math.PI) {
            directionRadians -= 2 * Math.PI;
        }
    }

    private void rotateRight(double durationMs) {
        directionRadians -= rotateSpeedPerSecond * (durationMs/1000);
        if(directionRadians < 0.0) {
            directionRadians += 2 * Math.PI;
        }
    }

    public void updateLocation(double durationMs) {
        if(movement.get(MovementEvent.MovementType.FORWARD)) {
            moveForward(durationMs);
        }
        if(movement.get(MovementEvent.MovementType.BACKWARD)) {
            moveBackward(durationMs);
        }
        if(movement.get(MovementEvent.MovementType.LEFT)) {
            rotateLeft(durationMs);
        }
        if(movement.get(MovementEvent.MovementType.RIGHT)) {
            rotateRight(durationMs);
        }
    }

    public void handleMovementEvent(MovementEvent event) {
        movement.put(event.getType(), event.isEnabled());
    }
}
