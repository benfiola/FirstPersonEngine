package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.utils.Rounder;

import java.awt.*;

/**
 * Created by Ben on 8/5/2015.
 */
public class CircleGraphicData extends AbstractGraphicData {
    private Point center;
    private Color color;
    private Double diameter;

    public CircleGraphicData(Point center, double diameter, Color color) {
        this.center = new Point(Rounder.round(center.getX() - diameter/2), Rounder.round(center.getY() - diameter/2));
        this.color = color;
        this.diameter = diameter;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(color);
        g.fillOval(Rounder.round(center.getX()), Rounder.round(center.getY()), Rounder.round(diameter), Rounder.round(diameter));
    }
}
