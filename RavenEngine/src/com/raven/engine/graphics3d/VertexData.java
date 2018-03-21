package com.raven.engine.graphics3d;

/**
 * Created by cookedbird on 11/8/17.
 */
public class VertexData {
    public enum Type {
        PLY, RAV
    }

    public Float x, y, z, nx, ny, nz, s, t, red, green, blue;

    public VertexData(String[] data, Type type) {
        switch (type) {
            case PLY:
                setPLYData(data);
                break;
            case RAV:
                setRAVData(data);
                break;
        }

    }

    public VertexData() {}

    private void setPLYData(String[] data) {
        x = Float.parseFloat(data[0]);
        y = Float.parseFloat(data[1]);
        z = Float.parseFloat(data[2]);
        nx = Float.parseFloat(data[3]);
        ny = Float.parseFloat(data[4]);
        nz = Float.parseFloat(data[5]);
        red = Integer.parseInt(data[6]) / 255f;
        green = Integer.parseInt(data[7]) / 255f;
        blue = Integer.parseInt(data[8]) / 255f;
    }

    private void setRAVData(String[] data) {
        x = Float.parseFloat(data[0]);
        y = Float.parseFloat(data[1]);
        z = Float.parseFloat(data[2]);
        nx = Float.parseFloat(data[3]);
        ny = Float.parseFloat(data[4]);
        nz = Float.parseFloat(data[5]);
        s = Float.parseFloat(data[6]);
        t = Float.parseFloat(data[7]);
        red = Float.parseFloat(data[8]);
        green = Float.parseFloat(data[9]);
        blue = Float.parseFloat(data[10]);
    }
}
