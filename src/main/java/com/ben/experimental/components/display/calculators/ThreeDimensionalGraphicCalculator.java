package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.components.display.graphicdata.LineGraphicData;
import com.ben.experimental.utils.Rounder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * Created by Ben on 8/6/2015.
 */
public class ThreeDimensionalGraphicCalculator extends AbstractGraphicCalculator {
    private static final Logger LOG = LogManager.getLogger(ThreeDimensionalGraphicCalculator.class);
    private static final double FOV = Math.PI/2;
    private static final double NEAR_DISTANCE = 1.0;
    private static final double FAR_DISTANCE = 10000.0;

    @Override
    /**
     * This saved my bacon.
     * http://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/projection-matrix-GPU-rendering-pipeline-clipping
     */
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize, Double zoomFactor) {
        LOG.info("new calculation");
        double aspectRatio = windowSize.getWidth() / windowSize.getHeight();
        ClippingMatrix c = new ClippingMatrix(aspectRatio, FOV, NEAR_DISTANCE, FAR_DISTANCE);

        //convert to camera coordinates
        SerializablePoint3D originalLocation = p.getLocation();
        double directionOffset = p.getDirectionRadians();
        p.setDirectionRadians(0.0);
        p.setLocation(new SerializablePoint3D(0.0, 0.0, 0.0));
        for (AbstractGeometry g : m.getGeometries()) {
            for (PointNode pointNode : g.getPoints()) {
                SerializablePoint3D curr = pointNode.getPoint();
                pointNode.setPoint(rotatePointAroundOrigin(translatePoint(curr, originalLocation.createInverse()), directionOffset));
            }
        }

        Queue<PointNode> queue = new LinkedList<PointNode>();
        Set<PointNode> visited = new HashSet<PointNode>();
        List<AbstractGraphicData> toReturn = new LinkedList<AbstractGraphicData>();
        SerializablePoint3D centerOfScreen = new SerializablePoint3D(windowSize.getWidth()/2, windowSize.getHeight()/2, 0.0);
        for(AbstractGeometry g : m.getGeometries()) {
            for(PointNode n : g.getPoints()) {
                queue.offer(n);
                while(!queue.isEmpty()) {
                    PointNode currNode = queue.poll();
                    if(!visited.contains(currNode)) {
                        HomogenousCoordinate curr = new HomogenousCoordinate(currNode.getPoint()).axisCorrection().perspectiveProjection(aspectRatio, FOV, NEAR_DISTANCE, FAR_DISTANCE);
                        for(PointNode neighborNode : currNode.getNeighbors()) {
                            if(!visited.contains(neighborNode)) {
                                HomogenousCoordinate neighbor = new HomogenousCoordinate(neighborNode.getPoint()).axisCorrection().perspectiveProjection(aspectRatio, FOV, NEAR_DISTANCE, FAR_DISTANCE);
                                curr = curr.clip(neighbor);
                                if (curr != null) {
                                    neighbor = neighbor.clip(curr);
                                    if (neighbor != null) {
                                        SerializablePoint3D currPt = curr.perspectiveDivide().toScreenCoordinates(windowSize);
                                        SerializablePoint3D neighborPt = neighbor.perspectiveDivide().toScreenCoordinates(windowSize);
                                        toReturn.add(new LineGraphicData(new Point(Rounder.round(currPt.getX()), Rounder.round(currPt.getY())), new Point(Rounder.round(neighborPt.getX()), Rounder.round(neighborPt.getY())), Color.ORANGE));
                                        queue.offer(neighborNode);
                                    }
                                } else {
                                    break;
                                }
                            }
                        }
                        visited.add(currNode);
                    }
                }
            }
        }
        return toReturn;
    }

    private class HomogenousCoordinate {

        private double x;
        private double y;
        private double z;
        private double w;
        private boolean alreadyPerspectiveDivided;

        public HomogenousCoordinate(double x, double y, double z, double w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.alreadyPerspectiveDivided = false;
        }

        public HomogenousCoordinate(SerializablePoint3D point3D) {
            this.x = point3D.getX();
            this.y = point3D.getY();
            this.z = point3D.getZ();
            this.w = 1.0;
        }

        public HomogenousCoordinate axisCorrection() {
            double temp = this.x;
            this.x = this.y;
            this.y = this.z;
            this.z = temp;
            return this;
        }

        public HomogenousCoordinate perspectiveProjection(double aspectRatio, double fov, double nearDistance, double farDistance) {
            this.x = this.x/(Math.tan(fov/2) * aspectRatio);
            this.y = this.y/(Math.tan(fov/2));
            this.w = this.z;
            this.z = (this.z * (farDistance)/(farDistance-nearDistance)) - ((farDistance*nearDistance)/(farDistance-nearDistance));

            return this;
        }


        public HomogenousCoordinate clip(HomogenousCoordinate other) {
            if(this.w <= 0.0 ) {
                return null;
            }

            double distance;
            double slopeX;
            double slopeY;
            double slopeZ;

            if(this.x < -this.w || this.x > this.w && this.x != other.x) {
                double newX;
                if(this.x < -this.w) {
                    distance = Math.abs(this.x-this.w);
                    newX = -this.w;
                } else {
                    distance = this.w - this.x;
                    newX = this.w;
                }
                slopeY = (other.y - this.y)/(other.x - this.x);
                slopeZ = (other.z - this.z)/(other.x - this.x);
                this.x = newX;
                this.y = this.y + (distance * slopeY);
                this.z = this.z + (distance * slopeZ);
            }

            if(this.y < -this.w || this.y > this.w) {
                double newY;
                if (this.y < -this.w) {
                    distance = Math.abs(this.y - this.w);
                    newY = -this.w;
                } else {
                    distance = this.w - this.x;
                    newY = this.w;
                }
                if(other.y != this.y) {
                    slopeX = (other.x - this.x)/(other.y - this.y);
                    slopeZ = (other.z - this.z)/(other.y - this.y);
                } else {
                    slopeX = 0.0;
                    slopeZ = 0.0;
                }

                this.y = newY;
                this.x = this.x + (distance * slopeX);
                this.z = this.z + (distance * slopeZ);
            }

            if(this.z < -this.w || this.z > this.w && this.z != other.z) {
                double newZ;
                if(this.z < -this.w) {
                    distance = Math.abs(this.z - this.w);
                    newZ = -this.w;
                } else {
                    distance = this.w - this.z;
                    newZ = this.w;
                }
                slopeX = (other.x - this.x)/(other.z - this.z);
                slopeY = (other.y - this.y)/(other.z - this.z);
                this.z = newZ;
                this.x = this.x + (distance * slopeX);
                this.y = this.y + (distance * slopeY);
            }

            return this;
        }

        public HomogenousCoordinate perspectiveDivide() {
            if(this.w != 1.0 && !this.alreadyPerspectiveDivided) {
                this.x = this.x / this.w;
                this.y = this.y / this.w;
                this.z = this.z / this.w;
            }
            this.alreadyPerspectiveDivided = true;
            return this;
        }

        public SerializablePoint3D toScreenCoordinates(Dimension windowSize){
            return new SerializablePoint3D((x+1)*.5*(windowSize.getWidth()), ((y+1))*.5*windowSize.getHeight(), 0.0);

        }
    }

    private class ClippingMatrix {
        private double aspectRatio;
        private double fov;
        private double nearDistance;
        private double farDistance;

        public ClippingMatrix(double aspectRatio, double fov, double nearDistance, double farDistance) {
            this.aspectRatio = aspectRatio;
            this.fov = fov;
            this.nearDistance = nearDistance;
            this.farDistance = farDistance;
        }

        public HomogenousCoordinate transform(SerializablePoint3D point) {
            double x = point.getY() / (Math.tan(fov/2) * aspectRatio);
            double y = point.getZ() / (Math.tan(fov/2));
            double z = (point.getX() * ((-nearDistance -farDistance)/(nearDistance - farDistance)) + ((2.0 * farDistance * nearDistance)/(nearDistance - farDistance)));
            double w = point.getX();

            HomogenousCoordinate toReturn = new HomogenousCoordinate(x, y, z, w);
            //LOG.info("Point at x:"+point.getX()+",y:"+point.getY()+",z:"+point.getZ()+",clipped:"+toReturn.withinClippingPlane());
            return toReturn;
        }

        public HomogenousCoordinate clip(HomogenousCoordinate toClip, HomogenousCoordinate other) {
            if(toClip.x < -1.0)  {
                double slopeY = (other.y - toClip.y)/(other.x - toClip.x);
                double slopeZ = (other.z - toClip.z)/(other.x - toClip.x);
                double distance = -1.0 - toClip.x;
                toClip = new HomogenousCoordinate(-1.0, toClip.y + (slopeY * distance), toClip.z + (slopeZ * distance), toClip.w);
            } else if(toClip.x > 1.0) {
                double slopeY = (other.y - toClip.y)/(other.x - toClip.x);
                double slopeZ = (other.z - toClip.z)/(other.x - toClip.x);
                double distance = toClip.x - 1.0;
                toClip = new HomogenousCoordinate(1.0, toClip.y - (slopeY * distance), toClip.z - (slopeZ * distance), toClip.w);
            }
            if(toClip.y < -1.0) {
                double slopeX = (other.x - toClip.x)/(other.y - toClip.y);
                double slopeZ = (other.z - toClip.z)/(other.y - toClip.y);
                double distance = -1.0 - toClip.y;
                toClip = new HomogenousCoordinate(toClip.x + (slopeX * distance), -1.0, toClip.z + (slopeZ * distance), toClip.w);
            } else if(toClip.y > 1.0) {
                double slopeX = (other.x - toClip.x)/(other.y - toClip.y);
                double slopeZ = (other.z - toClip.z)/(other.y - toClip.y);
                double distance = toClip.y - 1.0;
                toClip = new HomogenousCoordinate(toClip.x - (slopeX * distance), 1.0, toClip.z - (slopeZ * distance), toClip.w);
            }
            if(toClip.z <= 0.0) {
                double slopeX = (other.x - toClip.x)/(other.z - toClip.z);
                double slopeY = (other.y - toClip.y)/(other.z - toClip.z);
                double distance = 0.0 - toClip.z;
                toClip = new HomogenousCoordinate(toClip.x - (slopeX * distance), toClip.y - (slopeY * distance) , 0.0, toClip.w);
            } else if (toClip.z > 1.0) {
                double slopeX = (other.x - toClip.x)/(other.z - toClip.z);
                double slopeY = (other.y - toClip.y)/(other.z - toClip.z);
                double distance = toClip.z - 1.0;
                toClip = new HomogenousCoordinate(toClip.x + (slopeX * distance), toClip.y + (slopeY * distance) , 1.0, toClip.w);
            }
            return toClip;
        }
    }

}
