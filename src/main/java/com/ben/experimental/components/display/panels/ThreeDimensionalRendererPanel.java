package com.ben.experimental.components.display.panels;

import com.ben.experimental.components.display.calculators.ThreeDimensionalGraphicCalculator;
import com.ben.experimental.components.display.calculators.TwoDimensionalGraphicCalculator;
import com.ben.experimental.components.display.graphicdata.AbstractGraphicData;

import java.awt.*;

/**
 * Created by Ben on 8/5/2015.
 */
public class ThreeDimensionalRendererPanel extends AbstractRendererPanel {

    public ThreeDimensionalRendererPanel() {
        super(new ThreeDimensionalGraphicCalculator());
        this.zoomFactor = 60.0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.GRAY);
        g2d.drawString("3-D", 20, 20);
        for(AbstractGraphicData data : toDraw) {
            data.paint(g2d);
        }
    }
}
