package com.ben.experimental.components.controller.geometry;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 8/4/2015.
 */
public class Line extends AbstractGeometry {

    public Line(SerializablePoint3D p1, SerializablePoint3D p2) {
        super();
        PointNode pn1 = new PointNode(p1);
        PointNode pn2 = new PointNode(p2);
        pn1.addNeighbors(pn2);
        pn2.addNeighbors(pn1);
        points.add(pn1);
        points.add(pn2);
    }
}
