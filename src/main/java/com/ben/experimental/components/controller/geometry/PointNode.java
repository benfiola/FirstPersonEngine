package com.ben.experimental.components.controller.geometry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Ben on 8/4/2015.
 */
public class PointNode implements Serializable {
    private List<PointNode> neighbors;
    private SerializablePoint3D point;

    public PointNode(SerializablePoint3D point, PointNode ... neighbors) {
        this.point = point;
        this.neighbors = new ArrayList<PointNode>();
        this.neighbors.addAll(Arrays.asList(neighbors));
    }

    public PointNode(SerializablePoint3D point) {
        this(point, new PointNode[]{});
    }

    public PointNode(double x, double y, double z, PointNode ... neighbors) {
        this(new SerializablePoint3D(x, y, z), neighbors);
    }

    public PointNode(double x, double y, double z) {
        this(x, y, z, new PointNode[]{});
    }

    public SerializablePoint3D getPoint() {
        return this.point;
    }

    public void setPoint(SerializablePoint3D point) {
        this.point = point;
    }

    public List<PointNode> getNeighbors() {
        return this.neighbors;
    }

    public void addNeighbors(PointNode ... neighbors) {
        for(PointNode neighbor : neighbors) {
            this.neighbors.add(neighbor);
        }
    }
}
