package com.ben.experimental.components.display.calculators;

import com.ben.experimental.components.controller.entities.Map;
import com.ben.experimental.components.controller.entities.Player;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ben on 8/6/2015.
 */
public class TwoDimensionalGraphicCalculator extends AbstractGraphicCalculator {

    @Override
    public List<AbstractGraphicData> calculate(Player p, Map m) {
        normalize(p, m);
        return new ArrayList<AbstractGraphicData>();
    }
}
