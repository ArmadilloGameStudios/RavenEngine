package com.raven.sunny.scenes;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.VertexData;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.AmbientLight;
import com.raven.engine.scene.light.DirectionalLight;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.WorldObject;
import com.raven.sunny.Tree;
import com.raven.sunny.terrain.Terrain;
import com.raven.sunny.terrain.TerrainMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookedbird on 5/8/17.
 */
public class RandomScene extends Scene {
    float f = 0;
    private Terrain t;
    private ModelData water;
    private DirectionalLight sunLight, sunLight2;
    private AmbientLight ambientLight;

    public RandomScene() {
        super();

        t = TerrainMap.genTerrain(this, 60, 45);
        getLayerTerrain().addWorldObject(t);

        sunLight = new DirectionalLight();
        addLight(sunLight);
        sunLight2 = new DirectionalLight();
        addLight(sunLight2);

        ambientLight = new AmbientLight();
        ambientLight.intensity = .2f;
        //addLight(ambientLight);
    }

    @Override
    public List<ModelData> getSceneModels() {

        List<ModelData> mds = new ArrayList<>();

        ModelData land = t.getModelData();

        mds.add(land);

        // water
        water = new ModelData();

        float y = -.0f;
        float size = 500f;

        Float[] v = new Float[]{
                size, y, size,
                size, y, -size,
                -size, y, size,

                -size, y, size,
                size, y, -size,
                -size, y, -size,
        };

        Float[] n = new Float[]{
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,

                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,
        };

        Float[] c = new Float[]{
                .2f, .6f, .8f,
                .2f, .6f, .8f,
                .2f, .6f, .8f,

                .2f, .6f, .8f,
                .2f, .6f, .8f,
                .2f, .6f, .8f,
        };

        for (int i = 0; i < 18; i += 3) {
            VertexData vertexData = new VertexData();

            vertexData.x = v[i];
            vertexData.y = v[i + 1];
            vertexData.z = v[i + 2];
            vertexData.nx = n[i];
            vertexData.ny = n[i + 1];
            vertexData.nz = n[i + 2];
            vertexData.red = c[i];
            vertexData.green = c[i + 1];
            vertexData.blue = c[i + 2];

            water.addVertex(vertexData);
        }

        mds.add(water);

        getLayerWater().addWorldObject(new WorldObject(this, water) {

        });

        // load trees
        for (ModelData data : Tree.getModelData())
            mds.add(data);


        return mds;
    }

    @Override
    public void enterScene() {

    }

    @Override
    public void exitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        f += deltaTime;
        Vector3f dir = sunLight.direction;
        dir.z = (float) Math.cos(f / 10000f);
        dir.y = (float) Math.sin(f / 10000f);

        Vector3f color = sunLight.color;
        color.x = 1f;
        color.y = dir.y;
        color.z = (float) Math.pow(dir.y, 3.0);

        sunLight.intensity = Math.min(1f, Math.max(0f, dir.y * 1.5f + 1f));

        f += deltaTime;
        dir = sunLight2.direction;
        dir.z = (float) Math.cos(f / -10000f);
        dir.y = (float) Math.sin(f / -10000f);

        color = sunLight2.color;
        color.y = 1f;
        color.x = dir.y;
        color.z = (float) Math.pow(dir.y, 3.0);

        sunLight2.intensity = Math.min(1f, Math.max(0f, dir.y * 1.5f + 1f));
    }
}
