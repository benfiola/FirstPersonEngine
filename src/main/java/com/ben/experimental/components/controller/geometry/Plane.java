package com.ben.experimental.components.controller.geometry;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 8/4/2015.
 */
public class Plane extends AbstractGeometry {

    public Plane(List<PointNode> points) {
        super();
        points.addAll(points);
    }

    public Plane(PointNode ... points) {
        this(Arrays.asList(points));
    }
}
