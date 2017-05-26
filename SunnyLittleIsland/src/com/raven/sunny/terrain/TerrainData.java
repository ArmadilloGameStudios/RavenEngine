package com.raven.sunny.terrain;

import com.raven.engine.util.Vector3f;

/**
 * Created by cookedbird on 5/20/17.
 */
public class TerrainData {
    static final int Sand = 0, Grass = 1, Stone = 2, Snow = 3, Water = 4;
    static final int typeCount = 5;

    Vector3f position;
    int type;

    public static Float[] getColorOfType(int type) {
        switch (type) {
            case Grass:
                return new Float[] {0x01 / 255f, 0x99 / 255f, 0x0F / 255f};
            case Stone:
                return new Float[] {0x99 / 255f, 0x99 / 255f, 0x99 / 255f};
            case Snow:
                return new Float[] {0xFF / 255f, 0xFF / 255f, 0xFF / 255f};
            case Water:
                return new Float[] {0x40 / 255f, 0x40 / 255f, 0xB2 / 255f};
            case Sand:
            default:
                return new Float[] {0xE1 / 255f, 0xA9 / 255f, 0x5F / 255f};
        }
    }

    public static int getTypeFromTypes(int... types) {
        int[] counts = new int[typeCount];

        for (int t : types) {
            counts[t] += 1;
        }

        int highest = 0;
        int count = 0;

        for (int c = 0; c < counts.length; c++) {
            if (count < counts[c]) {
                highest = c;
                count = counts[c];
            }
        }

        return highest;
    }

    public static Float[] getColorOfTypes(int... types) {
        return getColorOfType(getTypeFromTypes(types));
    }
}
