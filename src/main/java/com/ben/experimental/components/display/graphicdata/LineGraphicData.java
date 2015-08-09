package com.ben.experimental.components.display.graphicdata;

import com.ben.experimental.utils.Rounder;

import java.awt.*;


/**
 * Created by Ben on 8/5/2015.
 */
public class LineGraphicData extends AbstractGraphicData {
    private Point start;
    private Point end;
    private Color color;

    public LineGraphicData(Point start, Point end, Color color) {
        this.start = start;
        this.end = end;
        this.color = color;
    }

    @Override
    public void paint(Graphics2D g) {
        g.setColor(color);
        g.drawLine(Rounder.round(start.getX()),
                Rounder.round(start.getY()),
                Rounder.round(end.getX()),
                Rounder.round(end.getY()));
    }
}
