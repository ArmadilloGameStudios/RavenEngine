package com.raven.sunny.terrain;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.util.SimplexNoise;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by cookedbird on 5/26/17.
 */
public class TerrainMap {
    private int width, height;
    private Vector3f[][] heightPoints;
    private TerrainData[][] data;


    public TerrainMap(int width, int height) {
        this.width = width;
        this.height = height;

        genMap();
    }

    public void genMap() {
        // Create starting vertices
        Random r = new Random();
        int seed = -377377594; //r.nextInt();
        // int seed = r.nextInt();
        System.out.println("Seed: " + seed);
        SimplexNoise noise = new SimplexNoise(seed);

        heightPoints = new Vector3f[width + 1][];

        for (int x = 0; x < width + 1; x++) {
            heightPoints[x] = new Vector3f[height + x % 2];

            for (int z = 0; z < height + x % 2; z++) {
                float x_pos = x - (width + 1) / 2;
                float z_pos = z - (height) / 2 - (x % 2) / 2f;

                float y_pos = 0f;

                for (int i = 1; i <= 4; i++) {

                    float scale = 14f / i;
                    float noise_height = 1.5f / (1 + i);

                    float noise_x = x_pos / scale + i * 2;
                    float noise_z = z_pos / scale + i * 2;

                    y_pos += noise.noise(noise_x, noise_z) * noise_height;
                }

                y_pos = (float)Math.exp(y_pos * 1.3) * (float)Math.max(0.0, (1.2 - (x_pos * x_pos + z_pos * z_pos) / 500.0));
                y_pos -= .45f;

                if (y_pos < 0f) {
                    y_pos -= .1;
                }

                heightPoints[x][z] = new Vector3f(x_pos, y_pos, z_pos);
            }
        }

        // Create faces
        data = new TerrainData[width][];
        for (int x = 0; x < width; x++) {
            data[x] = new TerrainData[height * 2 - 1];

            for (int z = 0; z < height * 2 - 1; z++) {
                TerrainData d = new TerrainData(this, x, z);
                if (x % 2 == 0) {
                    if (z % 2 == 0) {
                        d.setVertices(heightPoints[x + 1][z / 2], heightPoints[x][z / 2], heightPoints[x + 1][z / 2 + 1]);
                    } else {
                        d.setVertices(heightPoints[x + 1][z / 2 + 1], heightPoints[x][z / 2], heightPoints[x][z / 2 + 1]);
                    }
                } else {
                    if (z % 2 == 0) {
                        d.setVertices(heightPoints[x][z / 2], heightPoints[x][z / 2 + 1], heightPoints[x + 1][z / 2]);
                    } else {
                        d.setVertices(heightPoints[x][z / 2 + 1], heightPoints[x + 1][z / 2 + 1], heightPoints[x + 1][z / 2]);
                    }
                }

                data[x][z] = d;
            }
        }

        // Set Types
        for (TerrainData[] ds : data) {
            for (TerrainData d : ds) {
                if (d.getMinHeight() > .05f) {
                    d.setType(TerrainData.Grass);
                }

                if (d.getNormal().y < .6f) {
                    d.setType(TerrainData.Stone);
                }
            }
        }

        for (TerrainData[] ds : data) {
            for (TerrainData d : ds) {
                int stoneCount = 0;

                for (TerrainData adjD : d.getAdjacentTerrainData()) {
                    if (adjD.getType() == TerrainData.Stone)
                        stoneCount += 1;
                }

                if (stoneCount >= 2 && d.getType() != TerrainData.Stone) {
                    d.setType(TerrainData.Stone_add);
                }
            }
        }

        for (TerrainData[] ds : data) {
            for (TerrainData d : ds) {
                if (d.getType() == TerrainData.Stone_add) {
                    d.setType(TerrainData.Stone);
                }
            }
        }
    }

    public ModelData getModelData() {
        ModelData model = new ModelData();

        List<Float> vertices = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> colors = new ArrayList<>();

        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height * 2 - 1; z++) {
                TerrainData d = data[x][z];

                vertices.addAll(Arrays.asList(d.getVerticesAsArray()));

                Float[] normal = d.getNormal().toArray();
                normals.addAll(Arrays.asList(normal));
                normals.addAll(Arrays.asList(normal));
                normals.addAll(Arrays.asList(normal));

                Float[] color = d.getTypeColor();
                colors.addAll(Arrays.asList(color));
                colors.addAll(Arrays.asList(color));
                colors.addAll(Arrays.asList(color));
            }
        }

        model.setVertexData(vertices);
        model.setNormalData(normals);
        model.setColorData(colors);

        return model;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TerrainData[][] getTerrainData() {
        return this.data;
    }
}
