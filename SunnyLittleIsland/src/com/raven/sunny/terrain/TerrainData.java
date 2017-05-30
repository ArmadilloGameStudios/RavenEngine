package com.raven.sunny.terrain;

import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookedbird on 5/20/17.
 */
public class TerrainData {
    static final int Sand = 0, Grass = 1, Stone = 2, Stone_add = 3, Water = 4;

    private int x_arr, z_arr;

    private Vector3f[] vertices;
    private int type;

    private TerrainMap map;

    public TerrainData(TerrainMap map, int x, int z) {
        this.map=map;
        x_arr = x;
        z_arr = z;
    }

    public static Float[] getColorOfType(int type) {
        switch (type) {
            case Grass:
                return new Float[] {0x01 / 255f, 0x99 / 255f, 0x0F / 255f};
            case Stone:
                return new Float[] {0x99 / 255f, 0x99 / 255f, 0x99 / 255f};
            case Stone_add:
                return new Float[] {0xFF / 255f, 0xFF / 255f, 0xFF / 255f};
            case Water:
                return new Float[] {0x40 / 255f, 0x40 / 255f, 0xB2 / 255f};
            case Sand:
            default:
                return new Float[] {0xE1 / 255f, 0xC0 / 255f, 0x8F / 255f};
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

    public void setType(int type) {
        this.type = type;
    }

    public float getMinHeight() {
        float minHeight = vertices[0].y;

        for (int i = 1; i < vertices.length; i++) {
            if (minHeight > vertices[i].y) {
                minHeight = vertices[i].y;
            }
        }

        return minHeight;
    }

    public float getMaxHeight() {
        float maxHeight = vertices[0].y;

        for (int i = 1; i < vertices.length; i++) {
            if (maxHeight < vertices[i].y) {
                maxHeight = vertices[i].y;
            }
        }

        return maxHeight;
    }

    public TerrainData[] getAdjacentTerrainData() {
        List<TerrainData> adjList = new ArrayList<>();

        TerrainData[][] data = map.getTerrainData();

        int adjCount = 1;

        if ((x_arr % 2 + z_arr % 2) % 2 == 0 && x_arr != data.length - 1) {
            adjList.add(data[x_arr + 1][z_arr]);
        } else if (x_arr != 0) {
            adjList.add(data[x_arr - 1][z_arr]);
        } else {
            adjCount -= 1;
        }

        if (z_arr != 0) {
            adjList.add(data[x_arr][z_arr - 1]);
            adjCount += 1;
        }

        if (z_arr != data[x_arr].length - 1) {
            adjList.add(data[x_arr][z_arr + 1]);
            adjCount += 1;
        }

        TerrainData[] adj = new TerrainData[adjCount];
        return adjList.toArray(adj);
    }
}
