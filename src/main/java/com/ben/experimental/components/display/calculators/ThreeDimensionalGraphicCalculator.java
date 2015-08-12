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
    private static final double FOV = Math.PI/4;
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
            this.unit_w = w/w;
            this.w = w/w;
            this.withinClippingPlane = (Math.abs(this.x) <= Math.abs(this.unit_w) &&
                Math.abs(this.y) <= Math.abs(this.unit_w) &&
                0.0 <= this.z && this.z <= this.unit_w);
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
            double x = point.getY() * (fov * aspectRatio);
            double y = point.getZ() * (fov);
            double z = ((point.getX() * (((farDistance+nearDistance)/(farDistance-nearDistance)))) + ((2*nearDistance*farDistance)/(nearDistance-farDistance)));
            double w = point.getX();

            HomogenousCoordinate toReturn = new HomogenousCoordinate(x, y, z, w);
            //LOG.info("Point at x:"+point.getX()+",y:"+point.getY()+",z:"+point.getZ()+",clipped:"+toReturn.withinClippingPlane());
            return toReturn;
        }

        public HomogenousCoordinate clip(HomogenousCoordinate toClip, HomogenousCoordinate other) {

            if(toClip.x <= -toClip.unit_w) {
                toClip.x += (toClip.x + toClip.unit_w)/(toClip.x + other.x);
            } else if(toClip.x >= toClip.unit_w) {
                toClip.x -= (toClip.x - toClip.unit_w)/(toClip.x - other.x);
            }
            if(toClip.y <= -toClip.unit_w) {
                toClip.y += (toClip.y + toClip.unit_w)/(toClip.y + other.y);
            } else if(toClip.y >= toClip.w) {
                toClip.y -= (toClip.y - toClip.unit_w)/(toClip.y - other.y);
            }
            if(toClip.z <= 0.0) {
                toClip.z += (toClip.z + toClip.unit_w)/(toClip.z + other.z);
            } else if(toClip.z >= toClip.w) {
                toClip.z -= (toClip.z - toClip.unit_w)/(toClip.z - other.z);
            }
            return toClip;
        }
    }

}
