package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;

import java.awt.*;
import java.util.ArrayList;
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
        return new ArrayList<AbstractGraphicData>();
    }
}
