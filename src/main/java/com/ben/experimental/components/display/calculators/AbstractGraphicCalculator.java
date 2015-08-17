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
    public abstract List<AbstractGraphicData> calculate(Player p, Map m, Dimension windowSize);
}
