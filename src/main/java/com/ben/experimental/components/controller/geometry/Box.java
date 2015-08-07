package com.ben.experimental.components.controller.geometry;

import java.util.Arrays;

/**
 * Created by Ben on 8/4/2015.
 */
public class Box extends AbstractGeometry {

    /**
     * @param startingPoint if staring down at this box, this point represents the lowest, top, left point.
     * @param height
     * @param width
     * @param depth
     */
    public Box(SerializablePoint3D startingPoint, double height, double width, double depth) {
        super();
        PointNode ltl = new PointNode(startingPoint);
        PointNode ltr = new PointNode(new SerializablePoint3D(ltl.getPoint().getX() + width, ltl.getPoint().getY(), ltl.getPoint().getZ()));
        PointNode lbr = new PointNode(new SerializablePoint3D(ltr.getPoint().getX(), ltr.getPoint().getY(), ltr.getPoint().getZ() + depth));
        PointNode lbl = new PointNode(new SerializablePoint3D(ltl.getPoint().getX(), ltl.getPoint().getY(), ltl.getPoint().getZ() + depth));

        PointNode utl = new PointNode(new SerializablePoint3D(ltl.getPoint().getX(), ltl.getPoint().getY() + height, ltl.getPoint().getZ()));
        PointNode utr = new PointNode(new SerializablePoint3D(utl.getPoint().getX() + width, utl.getPoint().getY(), utl.getPoint().getZ()));
        PointNode ubr = new PointNode(new SerializablePoint3D(utr.getPoint().getX(), utr.getPoint().getY(), utr.getPoint().getZ() + depth));
        PointNode ubl = new PointNode(new SerializablePoint3D(utl.getPoint().getX(), utl.getPoint().getY(), utl.getPoint().getZ() + depth));

        ltl.addNeighbors(ltr, lbl, utl);
        ltr.addNeighbors(ltl, lbr, utr);
        lbr.addNeighbors(lbl, ltr, ubr);
        lbl.addNeighbors(lbr, ltl, ubl);
        utl.addNeighbors(utr, ubl, ltl);
        utr.addNeighbors(utl, ubr, ltr);
        ubr.addNeighbors(ubl, utr, lbr);
        ubl.addNeighbors(ubr, utl, lbl);

        points.addAll(Arrays.asList(new PointNode[]{ltl, ltr, lbr, lbl, utl, utr, ubr, ubl}));
    }
}
