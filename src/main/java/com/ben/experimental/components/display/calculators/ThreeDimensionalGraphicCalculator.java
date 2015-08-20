package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;
import com.ben.experimental.components.display.graphicdata.HomogenousCoordinate;
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
        double aspectRatio = windowSize.getWidth() / windowSize.getHeight();
        double fov = FOV;
        double nearDistance = NEAR_DISTANCE;
        double farDistance = FAR_DISTANCE;
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
                            HomogenousCoordinate currCoord = curr.getPoint().toHomogenousCoordinate().toCameraSpace(p).correctForView().toProjectionSpace(aspectRatio, fov, nearDistance, farDistance).normalize();
                            HomogenousCoordinate neighborCoord = neighbor.getPoint().toHomogenousCoordinate().toCameraSpace(p).correctForView().toProjectionSpace(aspectRatio, fov, nearDistance, farDistance).normalize();
//                            List<HomogenousCoordinate> clipped = new ArrayList<HomogenousCoordinate>();
//                            clipped.add(currCoord);
//                            clipped.add(neighborCoord);
                            List<HomogenousCoordinate> clipped = HomogenousCoordinate.clip(currCoord, neighborCoord);
                            if(clipped.size() == 2) {
                                Point currPt = clipped.get(0).toScreenCoordinates(windowSize);
                                Point neighborPt = clipped.get(1).toScreenCoordinates(windowSize);
                                toReturn.add(new LineGraphicData(currPt, neighborPt, Color.RED));
                            }
                            queue.offer(neighbor);
                        }
                    }
                }
            }
        }
        return toReturn;
    }
}
