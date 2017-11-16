package com.raven.sunny.scenes;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.VertexData;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.WorldObject;
import com.raven.sunny.SunnyLittleIslandGame;
import com.raven.sunny.Tree;
import com.raven.sunny.terrain.Terrain;
import com.raven.sunny.terrain.TerrainData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cookedbird on 5/8/17.
 */
public class RandomScene extends Scene {
    private Terrain t;
    private ModelData water;

    private Layer landLayer;
    private Layer waterLayer;

    public RandomScene() {
        super();

        landLayer = new Layer();
        this.addLayer(landLayer);

        waterLayer = new Layer(Layer.Destination.Water);
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

        waterLayer.addChild(new WorldObject(water) {

        });

        // load trees
        for (ModelData data : Tree.getModelData())
            mds.add(data);

        Random r = new Random();
        for (TerrainData[] tds : t.getTerrainMap().getTerrainData()) {
            for (TerrainData td : tds) {
                if (td.getType() == TerrainData.Sand && r.nextFloat() < .3f) {

                    WorldObject woTree = new Tree();
                    landLayer.addChild(woTree);

                    Vector3f center = td.getCenter();

                    woTree.setX(center.x);
                    woTree.setY(center.y);
                    woTree.setZ(center.z);
                    woTree.setScale(.35f);
                    woTree.setRotation(r.nextFloat() * 360);
                }

            }
        }

        return mds;
    }

    @Override
    public void enterScene() {

    }

    @Override
    public void exitScene() {

    }
}
