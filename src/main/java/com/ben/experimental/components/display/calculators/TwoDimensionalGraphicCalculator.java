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
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize) {
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
                            Point currPt = curr.getPoint().toHomogenousCoordinate().toCameraSpace(p).normalize(windowSize.getWidth() / 20, windowSize.getHeight() / 20, 1.0).toScreenCoordinates(windowSize);
                            Point neighborPt = neighbor.getPoint().toHomogenousCoordinate().toCameraSpace(p).normalize(windowSize.getWidth() / 20, windowSize.getHeight() / 20, 1.0).toScreenCoordinates(windowSize);
                            toReturn.add(new LineGraphicData(currPt, neighborPt, Color.RED));
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }

        Point player = p.getLocation().toHomogenousCoordinate().toCameraSpace(p).normalize(windowSize.getWidth() / 20, windowSize.getHeight() / 20, 1.0).toScreenCoordinates(windowSize);
        toReturn.add(new CircleGraphicData(player, 10.0, Color.BLUE));

        return toReturn;
    }
}
