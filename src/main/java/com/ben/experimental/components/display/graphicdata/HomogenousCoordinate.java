package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.utils.Rounder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 8/16/2015.
 */
public class HomogenousCoordinate {

    private static int LEFT_OUTCODE = 1;
    private static int RIGHT_OUTCODE = 2;
    private static int BOTTOM_OUTCODE = 4;
    private static int TOP_OUTCODE = 8;
    private static int NEAR_OUTCODE = 16;
    private static int FAR_OUTCODE = 32;

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

    public HomogenousCoordinate correctForView() {
        double newX = -this.y;
        double newY = this.z;
        double newZ = -this.x;
        double newW = this.w;

        return new HomogenousCoordinate(newX, newY, newZ, newW);
    }

    public HomogenousCoordinate toProjectionSpace(double aspectRatio, double fov, double nearDistance, double farDistance) {
        this.x = this.x / (Math.tan(fov / 2) * aspectRatio);
        this.y = this.y / (Math.tan(fov / 2));
        this.w = this.z;
        this.z = (this.z * (-farDistance) / (farDistance - nearDistance)) - ((-farDistance * nearDistance) / (farDistance - nearDistance));

        return this;
    }


    public static List<HomogenousCoordinate> clip(HomogenousCoordinate one, HomogenousCoordinate two) {
        List<HomogenousCoordinate> toReturn = new ArrayList<HomogenousCoordinate>();

        HomogenousCoordinate newOne = one;
        HomogenousCoordinate newTwo = two;

        int oneOutcode = one.computeOutcode();
        int twoOutcode = two.computeOutcode();
        while(true) {
            if((oneOutcode | twoOutcode) == 0) {
                toReturn.add(newOne);
                toReturn.add(newTwo);
                break;
            } else if( (oneOutcode & twoOutcode) != 0) {
                break;
            } else {
                double newX = 0.0;
                double newY = 0.0;
                double newZ = 0.0;

                int outcode = oneOutcode;
                newX = newOne.x;
                newY = newOne.y;
                newZ = newOne.z;
                double max = 1.0;
                if(oneOutcode == 0) {
                    outcode = twoOutcode;
                    newX = newTwo.x;
                    newY = newTwo.y;
                    newZ = newTwo.z;
                    max = 1.0;
                }

                if((outcode & TOP_OUTCODE) > 0) {
                    newX = newOne.x + (newTwo.x - newOne.x) * (-max - newOne.y)/(newTwo.y - newOne.y);
                    newY = -max;
//                    newZ = newOne.z + (newTwo.z - newTwo.z) * (-max - newOne.y)/(newTwo.y - newOne.y);
                } else if((outcode & BOTTOM_OUTCODE) > 0) {
                    newX = newOne.x + (newTwo.x - newOne.x) * (max - newOne.y)/(newTwo.y - newOne.y);
                    newY = max;
//                    newZ = newOne.z + (newTwo.z - newTwo.z) * (max - newOne.y)/(newTwo.y - newOne.y);
                } else if((outcode & LEFT_OUTCODE) > 0) {
                    newY = newOne.y + (newTwo.y - newOne.y) * (-max - newOne.x)/(newTwo.x - newOne.x);
//                    newZ = newOne.z + (newTwo.z - newOne.z) * (-max - newOne.x)/(newTwo.x - newOne.x);
                    newX = -max;
                } else if((outcode & RIGHT_OUTCODE) > 0) {
                    newY = newOne.y + (newTwo.y - newOne.y) * (max - newOne.x) / (newTwo.x - newOne.x);
//                    newZ = newOne.z + (newTwo.z - newOne.z) * (max - newOne.x)/(newTwo.x - newOne.x);
                    newX = max;
                }
//                } else if((outcode & NEAR_OUTCODE) > 0) {
//                    newX =  newOne.x + (newTwo.x - newOne.x) * (0.0 - newOne.z)/(newTwo.z - newOne.z);
//                    newY =  newOne.y + (newTwo.y - newOne.y) * (0.0 - newOne.z)/(newTwo.z - newOne.z);
//                    newZ = 0.0;
//                } else if((outcode & FAR_OUTCODE) > 0) {
//                    newX =  newOne.x + (newTwo.x - newOne.x) * (max - newOne.z)/(newTwo.z - newOne.z);
//                    newY =  newOne.y + (newTwo.y - newOne.y) * (max - newOne.z)/(newTwo.z - newOne.z);
//                    newZ = max;
//                }

                if(outcode == oneOutcode) {
                    newOne = new HomogenousCoordinate(newX, newY, newZ, max);
                    oneOutcode = newOne.computeOutcode();
                } else {
                    newTwo = new HomogenousCoordinate(newX, newY, newZ, max);
                    twoOutcode = newTwo.computeOutcode();
                }
            }
        }
        return toReturn;
    }

    private int computeOutcode() {
        int toReturn = 0;
        if(this.x < -1.0) {
            toReturn |= LEFT_OUTCODE;
        } else if(this.x > 1.0) {
            toReturn |= RIGHT_OUTCODE;
        }
        if(this.y < -1.0) {
            toReturn |= TOP_OUTCODE;
        } else if(this.y > 1.0){
            toReturn |= BOTTOM_OUTCODE;
        }
//        if(this.z < 0.0) {
//            toReturn |= NEAR_OUTCODE;
//        } else if(this.z > 1.0) {
//            toReturn |= FAR_OUTCODE;
//        }
        return toReturn;
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

    public HomogenousCoordinate normalize() {
        double newX = this.x;
        double newY = this.y;
        double newZ = this.z;
        double newW = this.w;

        if(this.w != 1.0) {
            newX = newX / this.w;
            newY = newY / this.w;
            newZ = newZ / this.w;
        }

        return new HomogenousCoordinate(newX, newY, newZ, newW);
    }

    public Point toScreenCoordinates(Dimension windowSize) {
        return new Point(Rounder.round((x + 1) * (windowSize.getWidth() / 2)), Rounder.round((y+1) * (windowSize.getHeight() / 2)));
    }
}
