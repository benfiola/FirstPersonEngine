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
    public List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize) {
        Queue<PointNode> queue = new LinkedList<PointNode>();
        Set<PointNode> visited = new HashSet<PointNode>();
        List<AbstractGraphicData> toReturn = new LinkedList<AbstractGraphicData>();
        for(AbstractGeometry g : m.getGeometries()) {
            for (PointNode n : g.getPoints()) {
                queue.offer(n);
                while (!queue.isEmpty()) {
                    PointNode curr = queue.poll();
                    if (!visited.contains(curr)) {
                        visited.add(curr);
                        for (PointNode neighbor : curr.getNeighbors()) {
                            Point currPt = curr.getPoint().toHomogenousCoordinate().toCameraSpace(p).toScreenCoordinates(windowSize);
                            Point neighborPt = neighbor.getPoint().toHomogenousCoordinate().toCameraSpace(p).toScreenCoordinates(windowSize);
                            toReturn.add(new LineGraphicData(currPt, neighborPt, Color.RED));
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        return toReturn;
    }
}
