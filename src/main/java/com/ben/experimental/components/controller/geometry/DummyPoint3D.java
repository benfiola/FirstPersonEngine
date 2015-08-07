package com.ben.experimental.components.controller.geometry;

import javafx.geometry.Point3D;

/**
 * To make Point3D serializable, I need a dummy superclass that provides a public no-args constructor and isn't serializable.
 */
public class DummyPoint3D extends Point3D {
    public DummyPoint3D() {
        super(0.0, 0.0, 0.0);
    }

    public DummyPoint3D(double x, double y, double z) {
        super(x, y, z);
    }
}
