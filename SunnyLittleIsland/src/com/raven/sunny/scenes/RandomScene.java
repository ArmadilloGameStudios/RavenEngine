package com.raven.sunny.scenes;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.worldobject.WorldObject;
import com.raven.sunny.SunnyLittleIslandGame;
import com.raven.sunny.terrain.Terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cookedbird on 5/8/17.
 */
public class RandomScene extends Scene {
    private Terrain t;
    private ModelData water;

    private Layer landLayer;
    private Layer waterLayer;

    public RandomScene(SunnyLittleIslandGame game) {
        super(game);

        landLayer = new Layer(this);
        this.addLayer(landLayer);

        waterLayer = new Layer(this, Layer.Destination.Water);
        this.addLayer(waterLayer);

        t = Terrain.genTerrain(landLayer, 60, 45);
        landLayer.addChild(t);
    }

    @Override
    public List<ModelData> getSceneModels() {

        List<ModelData> mds = new ArrayList<>();

        ModelData land = t.getModelData();

        mds.add(land);


        // water
        water = new ModelData();

        float y = -.03f;

        Float[] v = new Float[] {
                50f, y, 50f,
                50f, y, -50f,
                -50f, y, 50f,

                -50f, y, 50f,
                50f, y, -50f,
                -50f, y, -50f,
        };

        Float[] n = new Float[] {
                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,

                0f, 1f, 0f,
                0f, 1f, 0f,
                0f, 1f, 0f,
        };

        Float[] c = new Float[] {
                .2f, .6f, .8f,
                .2f, .6f, .8f,
                .2f, .6f, .8f,

                .2f, .6f, .8f,
                .2f, .6f, .8f,
                .2f, .6f, .8f,
        };

        water.setVertexData(Arrays.asList(v));
        water.setNormalData(Arrays.asList(n));
        water.setColorData(Arrays.asList(c));

        mds.add(water);

        waterLayer.addChild(new WorldObject(landLayer, water) {

        });

        return mds;
    }

    @Override
    public void enterScene() {

    }

    @Override
    public void exitScene() {

    }
}
