package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.utils.Rounder;

import java.awt.*;

/**
 * Created by Ben on 8/5/2015.
 */
public class PlayerGraphicData extends AbstractGraphicData {
    private Point center;
    private Color color;
    private int radius;

    public PlayerGraphicData(Point center, Color color) {
        this.center = center;
        this.color = color;
        this.radius = 5;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(this.color);
        g.drawOval(Rounder.round(center.getX()), Rounder.round(center.getY()), radius, radius);
    }
}
