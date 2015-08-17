package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.utils.Rounder;

import java.awt.*;

/**
 * Created by Ben on 8/16/2015.
 */
public class HomogenousCoordinate {

    private double x;
    private double y;
    private double z;
    private double w;

    public HomogenousCoordinate(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public HomogenousCoordinate toCameraSpace(Player p) {
        //assumes camera is pointing in the direction of the negative z-axis
        double c = Math.cos(p.getDirectionRadians());
        double s = Math.sin(p.getDirectionRadians());

        double translatedX = this.x - p.getLocation().getX();
        double translatedY = this.y - p.getLocation().getY();
        double translatedZ = this.z - p.getLocation().getZ();
        double translatedW = this.w;

        double newX = (translatedX * c) + (translatedY * s);
        double newY = (translatedX * s) - (translatedY * c);
        double newZ = translatedZ;
        double newW = translatedW;

        return new HomogenousCoordinate(newX, newY, newZ, newW);
    }

    public HomogenousCoordinate toProjectionSpace(double aspectRatio, double fov, double nearDistance, double farDistance) {
        this.x = this.x / (Math.tan(fov / 2) * aspectRatio);
        this.y = this.y / (Math.tan(fov / 2));
        this.w = this.z;
        this.z = (this.z * (farDistance) / (farDistance - nearDistance)) - ((farDistance * nearDistance) / (farDistance - nearDistance));

        return this;
    }


    public HomogenousCoordinate toClipSpace(HomogenousCoordinate other) {
        return this;
    }

    public HomogenousCoordinate normalize(double xNorm, double yNorm, double zNorm) {
        double newX = this.x;
        double newY = this.y;
        double newZ = this.z;
        double newW = this.w;

        if(xNorm != 1.0) {
            newX = newX / xNorm;
        }
        if(yNorm != 1.0) {
            newY = newY / yNorm;
        }
        if(zNorm != 1.0) {
            newZ = newZ/ zNorm;
        }
        return new HomogenousCoordinate(newX, newY, newZ, newW);
    }

    public Point toScreenCoordinates(Dimension windowSize) {
        return new Point(Rounder.round((x + 1) * (windowSize.getWidth() / 2)), Rounder.round((y+1) * (windowSize.getHeight() / 2)));
    }
}
