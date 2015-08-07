package com.ben.experimental.components.controller.entities;

import com.ben.experimental.components.controller.geometry.AbstractGeometry;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ben on 8/5/2015.
 */
public class Map implements Serializable {

    private List<AbstractGeometry> geometries;

    public Map(List<AbstractGeometry> geometries) {
        this.geometries = geometries;
    }

    public List<AbstractGeometry> getGeometries() {
        return this.geometries;
    }
}
