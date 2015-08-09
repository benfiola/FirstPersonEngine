package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.components.display.graphicdata.LineGraphicData;
import com.ben.experimental.components.display.graphicdata.PlayerGraphicData;
import com.ben.experimental.utils.Rounder;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Ben on 8/6/2015.
 */
public class TwoDimensionalGraphicCalculator extends AbstractGraphicCalculator {
    @Override
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize) {
        SerializablePoint3D originalLocation = p.getLocation();
        double directionOffset = p.getDirectionRadians();

        p.setDirectionRadians(0.0);
        p.setLocation(new SerializablePoint3D(0.0, 0.0, 0.0));
        for (AbstractGeometry g : m.getGeometries()) {
            for (PointNode pointNode : g.getPoints()) {
                SerializablePoint3D curr = pointNode.getPoint();
                pointNode.setPoint(rotatePoint(translatePoint(curr, originalLocation), p.getLocation(), directionOffset));
            }
        }

        Queue<PointNode> queue = new LinkedList<PointNode>();
        Set<PointNode> visited = new HashSet<PointNode>();
        List<AbstractGraphicData> toReturn = new LinkedList<AbstractGraphicData>();
        for(AbstractGeometry g : m.getGeometries()) {
            for(PointNode n : g.getPoints()) {
                queue.offer(n);
                while(!queue.isEmpty()) {
                    PointNode curr = queue.poll();
                    if(!visited.contains(curr)) {
                        visited.add(curr);
                        for (PointNode neighbor : curr.getNeighbors()) {
                            Point start = new Point(Rounder.round(curr.getPoint().getX() + (windowSize.getWidth() / 2)),
                                    Rounder.round(curr.getPoint().getY() + (windowSize.getHeight()/2)));
                            Point end = new Point(Rounder.round(neighbor.getPoint().getX() + (windowSize.getWidth() / 2)),
                                    Rounder.round(neighbor.getPoint().getY() + (windowSize.getHeight()/2)));
                            toReturn.add(new LineGraphicData(start, end, Color.BLACK));
                        }
                    }
                }
            }
        }

        toReturn.add(new PlayerGraphicData(new Point(Rounder.round(windowSize.getWidth()/2), Rounder.round(windowSize.getHeight()/2)), Color.BLUE));
        return toReturn;
    }
}
