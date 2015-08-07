package com.ben.experimental.utils;

import java.io.*;

/**
 * Created by Ben on 8/6/2015.
 */
public class ObjectCloner {
    public static Object clone(Object toClone) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(toClone);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
