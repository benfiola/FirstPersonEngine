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
     * http://stackoverflow.com/questions/724219/how-to-convert-a-3d-point-into-2d-perspective-projection
     */
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize, Double zoomFactor) {
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
                    PointNode curr = queue.poll();
                    if(!visited.contains(curr)) {
                        for(PointNode neighbor : curr.getNeighbors()) {
                            HomogenousCoordinate currCoord = c.transform(curr.getPoint());
                            HomogenousCoordinate neighborCoord = c.transform(neighbor.getPoint());

                            boolean clipCurr = currCoord.withinClippingPlane();
                            boolean clipNeighbor = neighborCoord.withinClippingPlane();
                            //clip here
                            if(!currCoord.withinClippingPlane() && !neighborCoord.withinClippingPlane()) {
                                continue;
                            } else if(!currCoord.withinClippingPlane()) {
                                currCoord = c.clip(currCoord, neighborCoord);
                            } else if(!neighborCoord.withinClippingPlane()) {
                                currCoord = c.clip(neighborCoord, currCoord);
                            }

                            //transform to screen coords
                            SerializablePoint3D currPt = new SerializablePoint3D( ((currCoord.x * windowSize.getWidth())/(2.0 * currCoord.w)), ((currCoord.y * windowSize.getHeight())/(2.0 * currCoord.w)), 0.0);
                            SerializablePoint3D neighborPt = new SerializablePoint3D( ((neighborCoord.x * windowSize.getWidth())/(2.0 * neighborCoord.w)), ((neighborCoord.y * windowSize.getHeight())/(2.0 * neighborCoord.w)), 0.0);
                            currPt = translatePoint(currPt, centerOfScreen);
                            neighborPt = translatePoint(neighborPt, centerOfScreen);
                            Point start = new Point(Rounder.round(currPt.getX()), Rounder.round(currPt.getY()));
                            Point end = new Point(Rounder.round(neighborPt.getX()), Rounder.round(neighborPt.getY()));
                            toReturn.add(new LineGraphicData(start, end, Color.ORANGE));
                            queue.offer(neighbor);
                        }
                        visited.add(curr);
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
        private double unit_w;

        private boolean withinClippingPlane = false;

        public HomogenousCoordinate(double x, double y, double z, double w) {
            this.x = x/w;
            this.y = y/w;
            this.z = z/w;
            this.w = w;
            this.withinClippingPlane = (Math.abs(this.x) <= Math.abs(1.0) &&
                Math.abs(this.y) <= Math.abs(1.0) &&
                0.0 <= this.z && this.z < 1.0);
        }

        public boolean withinClippingPlane() {
            return this.withinClippingPlane;
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
