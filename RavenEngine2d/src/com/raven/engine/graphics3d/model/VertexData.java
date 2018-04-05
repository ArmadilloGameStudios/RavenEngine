package com.raven.engine.graphics3d.model;

import javafx.util.Pair;

import java.util.*;

/**
 * Created by cookedbird on 11/8/17.
 */
public class VertexData {
    public enum Type {
        PLY, RAV
    }

    // Don't forget about the shader when adding more
    public Float x = 0f, y = 0f, z = 0f,
            nx = 0f, ny = 0f, nz = 0f,
            s = 0f, t = 0f,
            red = 0f, green = 0f, blue = 0f;
    public Integer[] b = new Integer[]{0, 0, 0, 0};
    public Float[] w = new Float[]{1f, 0f, 0f, 0f};

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

    public VertexData() {
    }

    private void setPLYData(String[] data) {
        x = Float.parseFloat(data[0]);
        y = Float.parseFloat(data[1]);
        z = Float.parseFloat(data[2]);
        nx = Float.parseFloat(data[3]);
        ny = Float.parseFloat(data[4]);
        nz = Float.parseFloat(data[5]);
        s = 0f;
        t = 0f;
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

        List<Pair<Integer, Float>> bones = new ArrayList<>();

        for (int i = 0; i + 14 < data.length; i += 2) {
            bones.add(new Pair<>(
                    Integer.parseInt(data[i + 12]),
                    Float.parseFloat(data[i + 13])));
        }

        bones.sort(new Comparator<Pair<Integer, Float>>() {
            @Override
            public int compare(Pair<Integer, Float> a, Pair<Integer, Float> b) {

                if (a.getValue() - b.getValue() == 0) {
                    return 0;
                } else if (a.getValue() - b.getValue() > 0) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < 4 && i < bones.size(); i++) {
            b[i] = bones.get(i).getKey();
            w[i] = bones.get(i).getValue();
        }
    }

    @Override
    public String toString() {
        StringBuilder cat = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            cat.append(b[i]).append(" ");
            cat.append(w[i]).append(" ");
        }

        return cat.toString();
    }
}
