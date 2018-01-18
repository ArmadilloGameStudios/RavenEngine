package com.raven.breakingsands.scenes;

import com.raven.breakingsands.Terrain;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class TestScene extends Scene {
    private GlobalDirectionalLight sunLight;
    private Terrain[][] terrain;
    private int size = 32;

    public TestScene() {
        super();

        sunLight = new GlobalDirectionalLight();
    }

    @Override
    public List<ModelData> getSceneModels() {
        List<ModelData> models = new ArrayList<>();

        // TODO
        models.addAll(Terrain.getModelData());
        models.addAll(Decal.getModelData());

        return models;
    }

    @Override
    public void enterScene() {
        sunLight.origin = new Vector3f(0, 2, 0);
        sunLight.color = new Vector3f(1, 1, 1);
        sunLight.intensity = 1f;
        sunLight.shadowTransparency = .2f;

        Vector3f dir = new Vector3f(1, 5, 2);
        sunLight.setDirection(dir.normalize());

        addLight(sunLight);

        // Terrain
        terrain = new Terrain[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                terrain[x][y] = new Terrain(this, "sand", x, y);

                terrain[x][y].setX(x * 2 - size);
                terrain[x][y].setZ(y * 2 - size);

                getLayerTerrain().addWorldObject(terrain[x][y]);
            }
        }

        // front left
        Decal decal = new Decal(this, "tall wall");
        terrain[7][8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][9].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][10].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][11].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][9].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][10].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][11].setDecal(decal);

        decal = new Decal(this, "tall corner");
        terrain[7][7].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(270);
        terrain[9][7].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(270);
        terrain[8][7].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(90);
        terrain[7][12].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(180);
        terrain[9][12].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(90);
        terrain[8][12].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][8].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][9].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][10].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][11].setDecal(decal);


        // front right
        decal = new Decal(this, "tall wall");
        terrain[7][8+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][9+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][10+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        terrain[7][11+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][8+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][9+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][10+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[9][11+8].setDecal(decal);

        decal = new Decal(this, "tall corner");
        terrain[7][7+8].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(270);
        terrain[9][7+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(270);
        terrain[8][7+8].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(90);
        terrain[7][12+8].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(180);
        terrain[9][12+8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(90);
        terrain[8][12+8].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][8+8].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][9+8].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][10+8].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[8][11+8].setDecal(decal);

        // Obelisks
        decal = new Decal(this, "obelisk");
        terrain[5][12].setDecal(decal);

        decal = new Decal(this, "obelisk");
        terrain[5][15].setDecal(decal);

        decal = new Decal(this, "obelisk");
        terrain[3][12].setDecal(decal);

        decal = new Decal(this, "obelisk");
        terrain[3][15].setDecal(decal);

        decal = new Decal(this, "obelisk");
        terrain[1][12].setDecal(decal);

        decal = new Decal(this, "obelisk");
        terrain[1][15].setDecal(decal);

        // short wall
        decal = new Decal(this, "tall short wall");
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[10][8].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[12][8].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[14][8].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[16][8].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[18][8].setDecal(decal);

        decal = new Decal(this, "tall short wall");
        terrain[19][8].setDecal(decal);

        decal = new Decal(this, "tall short wall");
        decal.setRotation(90);
        terrain[20][9].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[21][8].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(270);
        terrain[20][7].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[20][8].setDecal(decal);

        decal = new Decal(this, "tall corner");
        terrain[19][7].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(270);
        terrain[21][7].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(90);
        terrain[19][9].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(180);
        terrain[21][9].setDecal(decal);

        // short wall
        decal = new Decal(this, "tall short wall");
        decal.setRotation(180);
        terrain[9][19].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[10][19].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[12][19].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[14][19].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[16][19].setDecal(decal);

        decal = new Decal(this, "short wall");
        terrain[18][19].setDecal(decal);

        decal = new Decal(this, "tall short wall");
        terrain[19][19].setDecal(decal);

        decal = new Decal(this, "tall short wall");
        decal.setRotation(270);
        terrain[20][18].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(180);
        terrain[21][19].setDecal(decal);

        decal = new Decal(this, "tall wall");
        decal.setRotation(90);
        terrain[20][20].setDecal(decal);

        decal = new Decal(this, "tall ceiling");
        terrain[20][19].setDecal(decal);

        decal = new Decal(this, "tall corner");
        terrain[19][18].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(270);
        terrain[21][18].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(90);
        terrain[19][20].setDecal(decal);

        decal = new Decal(this, "tall corner");
        decal.setRotation(180);
        terrain[21][20].setDecal(decal);

        // back wall
        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][16].setDecal(decal);

        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][15].setDecal(decal);

        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][14].setDecal(decal);

        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][13].setDecal(decal);

        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][12].setDecal(decal);

        decal = new Decal(this, "short wall");
        decal.setRotation(90);
        terrain[20][11].setDecal(decal);
    }

    @Override
    public void exitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }
}
