package com.raven.sunny.scenes;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.WorldObject;
import com.raven.sunny.SunnyLittleIslandGame;
import com.raven.sunny.terrain.Terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by cookedbird on 5/8/17.
 */
public class RandomScene extends Scene {
    private Terrain t;

    public RandomScene(SunnyLittleIslandGame game) {
        super(game);

        Layer l = new Layer(this);
        this.addLayer(l);

        t = Terrain.genTerrain(l, 55, 55);
        l.addChild(t);
    }

    @Override
    public List<ModelData> getSceneModels() {

        List<ModelData> mds = new ArrayList<>();

        ModelData land = t.getModelData();

        mds.add(land);

        return mds;
    }

    @Override
    public void enterScene() {

    }

    @Override
    public void exitScene() {

    }
}
