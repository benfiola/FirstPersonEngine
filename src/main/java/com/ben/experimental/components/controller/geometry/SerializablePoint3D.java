package com.ben.experimental.components.controller.geometry;

import com.ben.experimental.components.display.graphicdata.HomogenousCoordinate;
import com.ben.experimental.utils.ObjectCloner;
import javafx.geometry.Point3D;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * Created by Ben on 8/6/2015.
 */
public class SerializablePoint3D extends DummyPoint3D implements Serializable {
    public SerializablePoint3D() {
        super(-1.0, -1.0, -1.0);
    }

    public SerializablePoint3D(double x, double y, double z) {
        super(x, y, z);
    }

    private void writeObject(ObjectOutputStream out) throws IOException  {
        out.writeDouble(this.getX());
        out.writeDouble(this.getY());
        out.writeDouble(this.getZ());
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        setX(in.readDouble());
        setY(in.readDouble());
        setZ(in.readDouble());
    }

    private void setField(String field, double value) {
        try {
            Field f = Point3D.class.getDeclaredField(field);
            f.setAccessible(true);
            f.setDouble(this, value);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setX(double x) {
        setField("x", x);
    }

    private void setY(double y) {
        setField("y", y);
    }

    private void setZ(double z) {
        setField("z", z);
    }

    public HomogenousCoordinate toHomogenousCoordinate() {
        return new HomogenousCoordinate(getX(), getY(), getZ(), 1.0);
    }

}
