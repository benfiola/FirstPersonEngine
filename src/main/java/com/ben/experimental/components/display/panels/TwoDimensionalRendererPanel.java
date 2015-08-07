package com.ben.experimental.components.display.panels;

import com.ben.experimental.components.display.calculators.AbstractGraphicCalculator;
import com.ben.experimental.components.display.calculators.TwoDimensionalGraphicCalculator;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;

import java.awt.*;

/**
 * Created by Ben on 8/5/2015.
 */
public class TwoDimensionalRendererPanel extends AbstractRendererPanel {

    public TwoDimensionalRendererPanel() {
        super();
        setBackground(Color.BLACK);
        setVisible(true);
    }

    @Override
    protected AbstractGraphicCalculator getGraphicsCalculator() {
        return new TwoDimensionalGraphicCalculator();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        for(AbstractGraphicData data : toDraw) {
            data.paint(g2d);
        }
    }
}
