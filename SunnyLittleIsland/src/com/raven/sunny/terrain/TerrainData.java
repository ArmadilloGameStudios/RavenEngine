package com.raven.sunny.terrain;

import com.raven.engine.util.Vector3f;

/**
 * Created by cookedbird on 5/20/17.
 */
public class TerrainData {
    static final int Sand = 0, Grass = 1, Stone = 2, Snow = 3, Water = 4;
    static final int typeCount = 5;

    private Vector3f[] vertices;
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

    public int getType() {
        return type;
    }

    public Float[] getTypeColor() {
        return getColorOfType(getType());
    }

    public void setVertices(Vector3f... vertices) {
        this.vertices = vertices;
    }

    public Float[] getVerticesAsArray() {
        Float[] arr = new Float[vertices.length * 3];

        for (int i = 0; i < vertices.length; i++) {
            arr[i * 3 + 0] = vertices[i].x;
            arr[i * 3 + 1] = vertices[i].y;
            arr[i * 3 + 2] = vertices[i].z;
        }

        return arr;
    }

    public Vector3f getNormal() {
        // assuming 3 vectors
        return vertices[1].subtract(vertices[0]).cross(vertices[2].subtract(vertices[0])).normalize();
    }
}
