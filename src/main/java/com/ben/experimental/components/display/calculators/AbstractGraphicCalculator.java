package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.controller.geometry.AbstractGeometry;
import com.ben.experimental.components.controller.geometry.PointNode;
import com.ben.experimental.components.controller.geometry.SerializablePoint3D;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;

import java.awt.*;
import java.util.List;

/**
 * Created by Ben on 8/6/2015.
 */
public abstract class AbstractGraphicCalculator {

    protected SerializablePoint3D translatePoint(SerializablePoint3D toTranslate, SerializablePoint3D vector) {
        return new SerializablePoint3D(toTranslate.getX() + vector.getX(), toTranslate.getY() + vector.getY(), toTranslate.getZ() + vector.getZ());
    }

    protected SerializablePoint3D rotatePoint(SerializablePoint3D toRotate, SerializablePoint3D origin, double direction) {
        double c = Math.cos(direction);
        double s = Math.sin(direction);

        SerializablePoint3D translated = translatePoint(toRotate, origin.createInverse());
        double newX = (translated.getX() * c) + (translated.getY() * s);
        double newY = (translated.getX() * s) - (translated.getY() * c);
        double newZ = translated.getZ();

        SerializablePoint3D rotated = new SerializablePoint3D(newX, newY, newZ);
        return translatePoint(rotated, origin);
    }

    protected SerializablePoint3D rotatePointAroundOrigin(SerializablePoint3D toRotate, double direction) {
        return rotatePoint(toRotate, new SerializablePoint3D(0.0, 0.0, 0.0), direction);
    }

    public abstract List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize, Double zoomFactor);
}
