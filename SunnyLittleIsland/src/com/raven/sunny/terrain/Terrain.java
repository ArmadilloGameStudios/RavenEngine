package com.raven.sunny.terrain;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.util.SimplexNoise;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by cookedbird on 5/20/17.
 */
public class Terrain extends WorldObject {
    TerrainData[][] data;
    ModelData model;

    private Terrain(Parentable parent, ModelData model) {
        super(parent, model);

        this.model = model;
    }

    public static Terrain genTerrain(Parentable parent, int width, int height) {
        ModelData model = new ModelData();

        Terrain terrain = new Terrain(parent, model);
        terrain.setWidth(width);
        terrain.setHeight(height);
        terrain.data = new TerrainData[width][];

        Random r = new Random();
        SimplexNoise noise = new SimplexNoise(r.nextInt());

        for (int x = 0; x < width; x++) {
            terrain.data[x] = new TerrainData[height + x % 2];

            for (int z = 0; z < height + x % 2; z++) {
                TerrainData data = terrain.data[x][z] = new TerrainData();

                float x_pos = x - width / 2;
                float z_pos = z - height / 2 - (x % 2) / 2f;

                float y_pos = 0f;

                for (int i = 1; i <= 4; i++) {

                    float scale = 14f / i;
                    float noise_height = 1.5f / (1 + i);

                    float noise_x = x_pos / scale + i * 2;
                    float noise_z = z_pos / scale + i * 2;

                    y_pos += noise.noise(noise_x, noise_z) * noise_height;
                }

                y_pos = (float)Math.exp(y_pos * 1.3) * (float)Math.max(0.0, (1.2 - (x_pos * x_pos + z_pos * z_pos) / 500.0));
                data.position = new Vector3f(x_pos, y_pos, z_pos);

                if (y_pos > 3.5) {
                    data.type = TerrainData.Snow;
                } else if (y_pos > 1.5) {
                    data.type = TerrainData.Stone;
                } else if (y_pos > .6) {
                    data.type = TerrainData.Grass;
                } else if (y_pos < .3) {
                    data.type = TerrainData.Water;
                } else {
                    data.type = TerrainData.Sand;
                }
            }
        }

        List<Float> vertices = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Float> colors = new ArrayList<>();

        for (int x = 1; x < width; x++) {
            for (int z = 0; z < height + x % 2; z++) {

                if (x % 2 == 0) {
                    TerrainData a = terrain.data[x][z];
                    TerrainData b = terrain.data[x - 1][z];
                    TerrainData c = terrain.data[x - 1][z + 1];

                    vertices.addAll(Arrays.asList(a.position.toArray()));
                    vertices.addAll(Arrays.asList(b.position.toArray()));
                    vertices.addAll(Arrays.asList(c.position.toArray()));

                    Vector3f normal = b.position.subtract(a.position).cross(c.position.subtract(a.position));
                    normals.addAll(Arrays.asList(normal.toArray()));
                    normals.addAll(Arrays.asList(normal.toArray()));
                    normals.addAll(Arrays.asList(normal.toArray()));

                    Float[] color = TerrainData.getColorOfTypes(a.type, b.type, c.type);
                    colors.addAll(Arrays.asList(color));
                    colors.addAll(Arrays.asList(color));
                    colors.addAll(Arrays.asList(color));

                    if (z != height - 1) {
                        a = terrain.data[x][z];
                        b = terrain.data[x - 1][z + 1];
                        c = terrain.data[x][z + 1];

                        vertices.addAll(Arrays.asList(a.position.toArray()));
                        vertices.addAll(Arrays.asList(b.position.toArray()));
                        vertices.addAll(Arrays.asList(c.position.toArray()));

                        normal = b.position.subtract(a.position).cross(c.position.subtract(a.position));
                        normals.addAll(Arrays.asList(normal.toArray()));
                        normals.addAll(Arrays.asList(normal.toArray()));
                        normals.addAll(Arrays.asList(normal.toArray()));

                        color = TerrainData.getColorOfTypes(a.type, b.type, c.type);
                        colors.addAll(Arrays.asList(color));
                        colors.addAll(Arrays.asList(color));
                        colors.addAll(Arrays.asList(color));
                    }
                } else if (z != 0) {
                    TerrainData a = terrain.data[x][z];
                    TerrainData b = terrain.data[x][z - 1];
                    TerrainData c = terrain.data[x - 1][z - 1];

                    vertices.addAll(Arrays.asList(a.position.toArray()));
                    vertices.addAll(Arrays.asList(b.position.toArray()));
                    vertices.addAll(Arrays.asList(c.position.toArray()));

                    Vector3f normal = b.position.subtract(a.position).cross(c.position.subtract(a.position));
                    normals.addAll(Arrays.asList(normal.toArray()));
                    normals.addAll(Arrays.asList(normal.toArray()));
                    normals.addAll(Arrays.asList(normal.toArray()));

                    Float[] color = TerrainData.getColorOfTypes(a.type, b.type, c.type);
                    colors.addAll(Arrays.asList(color));
                    colors.addAll(Arrays.asList(color));
                    colors.addAll(Arrays.asList(color));

                    if (z != height) {
                        a = terrain.data[x][z];
                        b = terrain.data[x - 1][z - 1];
                        c = terrain.data[x - 1][z];

                        vertices.addAll(Arrays.asList(a.position.toArray()));
                        vertices.addAll(Arrays.asList(b.position.toArray()));
                        vertices.addAll(Arrays.asList(c.position.toArray()));

                        normal = b.position.subtract(a.position).cross(c.position.subtract(a.position));
                        normals.addAll(Arrays.asList(normal.toArray()));
                        normals.addAll(Arrays.asList(normal.toArray()));
                        normals.addAll(Arrays.asList(normal.toArray()));

                        color = TerrainData.getColorOfTypes(a.type, b.type, c.type);
                        colors.addAll(Arrays.asList(color));
                        colors.addAll(Arrays.asList(color));
                        colors.addAll(Arrays.asList(color));
                    }
                }
            }
        }

        model.setVertexData(vertices);
        model.setNormalData(normals);
        model.setColorData(colors);

        return terrain;
    }

    public ModelData getModelData() {
        return model;
    }
}
