package com.raven.sunny.terrain;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

/**
 * Created by cookedbird on 5/20/17.
 */
public class Terrain extends WorldObject {
    TerrainMap map;
    ModelData model;

    private Terrain(Parentable parent, ModelData model) {
        super(parent, model);

        this.model = model;
    }

    public static Terrain genTerrain(Parentable parent, int width, int height) {
        TerrainMap map = new TerrainMap(width, height);

        Terrain terrain = new Terrain(parent, map.getModelData());
        terrain.setWidth(width);
        terrain.setHeight(height);
        terrain.map = map;

        return terrain;
    }

    public ModelData getModelData() {
        return model;
    }
}
