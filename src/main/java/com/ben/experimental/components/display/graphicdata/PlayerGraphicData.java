package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.utils.Rounder;
import javafx.geometry.Point2D;

import java.awt.*;

/**
 * Created by Ben on 8/5/2015.
 */
public class PlayerGraphicData extends AbstractGraphicData {
    private Point2D center;
    private Color color;
    private int radius;

    public PlayerGraphicData(Point2D center) {
        this.center = center;
        this.color = Color.YELLOW;
        this.radius = 5;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(this.color);
        g.drawOval(Rounder.round(center.getX()), Rounder.round(center.getY()), radius, radius);
    }
}
