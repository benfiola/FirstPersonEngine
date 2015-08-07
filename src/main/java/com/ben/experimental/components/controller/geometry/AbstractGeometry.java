package com.ben.experimental.components.controller.geometry;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Ben on 8/4/2015.
 */
public abstract class AbstractGeometry implements Serializable {
    List<PointNode> points;

    protected AbstractGeometry() {
        points = new ArrayList<PointNode>();
    }

    public List<PointNode> getPoints() {
        return this.points;
    }
}
