package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.components.display.graphicdata.LineGraphicData;
import com.ben.experimental.components.display.graphicdata.CircleGraphicData;
import com.ben.experimental.utils.Rounder;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Ben on 8/6/2015.
 */
public class TwoDimensionalGraphicCalculator extends AbstractGraphicCalculator {

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
                            SerializablePoint3D startPt3d = translatePoint(curr.getPoint().multiplyByScalar(zoomFactor), centerOfScreen);
                            SerializablePoint3D endPt3d = translatePoint(neighbor.getPoint().multiplyByScalar(zoomFactor), centerOfScreen);
                            Point start = new Point(Rounder.round(startPt3d.getX()), Rounder.round(startPt3d.getY()));
                            Point end = new Point(Rounder.round(endPt3d.getX()), Rounder.round(endPt3d.getY()));
                            toReturn.add(new LineGraphicData(start, end, Color.RED));
                        }
                    }
                }
            }
        }

        toReturn.add(new CircleGraphicData(new Point(Rounder.round(centerOfScreen.getX()), Rounder.round(centerOfScreen.getY())), zoomFactor, Color.BLUE));

        double losX = Math.cos(p.getDirectionRadians()) * 2;
        double losY = Math.sin(p.getDirectionRadians()) * 2;
        SerializablePoint3D los = translatePoint(new SerializablePoint3D(losX, losY, 0.0).multiplyByScalar(zoomFactor), centerOfScreen);

        toReturn.add(new LineGraphicData(new Point(Rounder.round(centerOfScreen.getX()), Rounder.round(centerOfScreen.getY())), new Point(Rounder.round(los.getX()), Rounder.round(los.getY())), Color.WHITE));

        return toReturn;
    }
}
