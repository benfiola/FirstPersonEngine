package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.components.display.graphicdata.CircleGraphicData;
import com.ben.experimental.components.display.graphicdata.LineGraphicData;
import com.ben.experimental.utils.Rounder;
import javafx.geometry.Point2D;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/**
 * Created by Ben on 8/6/2015.
 */
public class ThreeDimensionalGraphicCalculator extends AbstractGraphicCalculator {
    private static final Double MAX_DISTANCE = 10000.0;
    private static final Double MIN_DISTANCE = 1.0;
    private static final Double FOV_VERTICAL = (Math.PI/1.5);
    private static final Double FOV_HORIZONTAL = (Math.PI/1.5);
    @Override
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize, Double zoomFactor) {
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
                        visited.add(curr);
                        for (PointNode neighbor : curr.getNeighbors()) {
                            //x is the 'distance' axis
                            //y is our 'x' axis
                            //z is our 'y' axis
                            Double currDistance = curr.getPoint().getX() + 1.0;
                            Double neighborDistance = neighbor.getPoint().getX() + 1.0;

                            SerializablePoint3D currLimits = getLimitsAtDistance(currDistance);
                            SerializablePoint3D neighborLimits = getLimitsAtDistance(neighborDistance);

                            SerializablePoint3D curr3d = new SerializablePoint3D(curr.getPoint().getY() / currDistance, curr.getPoint().getZ()/currDistance, 0.0);
                            SerializablePoint3D neighbor3d = new SerializablePoint3D(neighbor.getPoint().getY() / neighborDistance, neighbor.getPoint().getZ()/neighborDistance, 0.0);

                            if(isValid(curr3d) && isValid(neighbor3d)) {
                                //no need to clip
                                curr3d = translatePoint(curr3d.multiplyByScalar(zoomFactor), centerOfScreen);
                                neighbor3d = translatePoint(neighbor3d.multiplyByScalar(zoomFactor), centerOfScreen);

                                Point start = new Point(Rounder.round(curr3d.getX()), Rounder.round(curr3d.getY()));
                                Point end = new Point(Rounder.round(neighbor3d.getX()), Rounder.round(neighbor3d.getY()));
                                toReturn.add(new LineGraphicData(start, end, Color.ORANGE));
                            } else if(isValid(curr3d) || isValid(neighbor3d) ) {
                                //need to clip
                            }
                        }
                    }
                }
            }
        }
        return toReturn;
    }

    private boolean isInDistanceBounds(SerializablePoint3D point) {
        Double distance = point.getX() + 1.0;
        return distance >= MIN_DISTANCE && distance <= MAX_DISTANCE;
    }

    private boolean isWithinClippingRegion(SerializablePoint3D point) {
        Double distance = point.getX() + 1.0;
        SerializablePoint3D limits = getLimitsAtDistance(distance);
        return (Math.abs(point.getY()) <= Math.abs(limits.getY()) && Math.abs(point.getZ()) <= Math.abs(limits.getZ()));
    }

    private boolean isValid(SerializablePoint3D point) {
        return isInDistanceBounds(point) && isWithinClippingRegion(point);
    }

    private SerializablePoint3D getLimitsAtDistance(Double distance) {
        Double xTan = Math.tan(FOV_HORIZONTAL/2);
        Double yTan = Math.tan(FOV_VERTICAL/2);

        Double minXDistance = xTan * MIN_DISTANCE;
        Double minYDistance = yTan * MIN_DISTANCE;
        Double maxXDistance = xTan * MAX_DISTANCE;
        Double maxYDistance = yTan * MAX_DISTANCE;

        //unit vectoring this line representing our 'limits'
        Double vX = maxXDistance - minXDistance;
        Double vY = maxYDistance - minYDistance;
        Double dist = Math.sqrt(Math.pow(vX, 2) + Math.pow(vY, 2));
        vX = vX/dist;
        vY = vY/dist;

        Double xResult = minXDistance + (vX * distance);
        Double yResult = minYDistance + (vY * distance);
        //using this unit vector to do this calculation.
        return new SerializablePoint3D(0.0, xResult, yResult);
    }
}
