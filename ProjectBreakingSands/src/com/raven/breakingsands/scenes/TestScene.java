package com.raven.breakingsands.scenes;

import com.raven.breakingsands.scenes.decal.Decal;
import com.raven.breakingsands.scenes.decal.DecalFactory;
import com.raven.breakingsands.scenes.pawn.Pawn;
import com.raven.breakingsands.scenes.pawn.PawnFactory;
import com.raven.breakingsands.scenes.terrain.Terrain;
import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Vector3f;
import com.raven.engine.util.pathfinding.Path;
import com.raven.engine.util.pathfinding.PathFinder;
import com.raven.engine.worldobject.Highlight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestScene extends Scene {
    public static Highlight
            OFF = new Highlight(),
            BLUE = new Highlight(.2f, .6f, 1f, .75f),
            BLUE_CHANGING = new Highlight(.2f, .6f, 1f, .5f),
            RED = new Highlight(1f, .3f, .2f, .75f),
            RED_CHANGING = new Highlight(1f, .3f, .2f, .5f),
            YELLOW = new Highlight(1f, .8f, .2f, .75f),
            YELLOW_CHANGING = new Highlight(1f, .8f, .2f, .5f),
            GREEN = new Highlight(.3f, 1f, .2f, .75f),
            GREEN_CHANGING = new Highlight(.3f, 1f, .2f, .5f);


    private GlobalDirectionalLight sunLight;
    private Terrain[][] terrain;

    private List<Pawn> pawns = new ArrayList<>();
    private Pawn activePawn;

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
        models.addAll(Pawn.getModelData());

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

        PawnFactory pf = new PawnFactory(this);
        Pawn p = pf.getInstance();
        pawns.add(p);

        terrain[28][28].setPawn(p);

        DecalFactory f = new DecalFactory(this);
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("straight");

        // front left
        Decal decal = f.getInstance();
        terrain[7][8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][9].setDecal(decal);

        decal = f.getInstance();
        terrain[7][10].setDecal(decal);

        decal = f.getInstance();
        terrain[7][11].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][10].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][11].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][20].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[8][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[8][12].setDecal(decal);

        decal = f.getInstance();
        terrain[7][8+8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][9+8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][10+8].setDecal(decal);

        decal = f.getInstance();
        terrain[7][11+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][9+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][10+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][11+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[8][7+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[20][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[8][12+8].setDecal(decal);

        f.clear();

        // Tall Corners
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("corner");

        decal = f.getInstance();
        terrain[7][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[9][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[7][12].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][12].setDecal(decal);

        decal = f.getInstance();
        terrain[7][7+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[9][7+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[7][12+8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][12+8].setDecal(decal);

        decal = f.getInstance();
        terrain[19][18].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[21][18].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[19][20].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][20].setDecal(decal);

        decal = f.getInstance();
        terrain[19][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[21][7].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[19][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[21][9].setDecal(decal);

        f.clear();

        // Tall Ceiling
        f.addTypeRestriction("tall");
        f.addTypeRestriction("ceiling");

        decal = f.getInstance();
        terrain[8][8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][9].setDecal(decal);

        decal = f.getInstance();
        terrain[8][10].setDecal(decal);

        decal = f.getInstance();
        terrain[8][11].setDecal(decal);

        decal = f.getInstance();
        terrain[8][8+8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][9+8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][10+8].setDecal(decal);

        decal = f.getInstance();
        terrain[8][11+8].setDecal(decal);

        decal = f.getInstance();
        terrain[20][8].setDecal(decal);

        decal = f.getInstance();
        terrain[20][19].setDecal(decal);

        f.clear();

        // Obelisks
        f.addTypeRestriction("obelisk");

        decal = f.getInstance();
        terrain[5][12].setDecal(decal);

        decal = f.getInstance();
        terrain[5][15].setDecal(decal);

        decal = f.getInstance();
        terrain[3][12].setDecal(decal);

        decal = f.getInstance();
        terrain[3][15].setDecal(decal);

        decal = f.getInstance();
        terrain[1][12].setDecal(decal);

        decal = f.getInstance();
        terrain[1][15].setDecal(decal);

        f.clear();

        // short tall transition
        f.addTypeRestriction("short");
        f.addTypeRestriction("tall");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("transition");

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][8].setDecal(decal);

        decal = f.getInstance();
        terrain[19][8].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][9].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(180);
        terrain[9][19].setDecal(decal);

        decal = f.getInstance();
        terrain[19][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(270);
        terrain[20][18].setDecal(decal);

        f.clear();

        // Short wall
        f.addTypeRestriction("short");
        f.addTypeRestriction("wall");
        f.addTypeRestriction("straight");

        decal = f.getInstance();
        terrain[10][8].setDecal(decal);

        decal = f.getInstance();
        terrain[12][8].setDecal(decal);

        decal = f.getInstance();
        terrain[14][8].setDecal(decal);

        decal = f.getInstance();
        terrain[16][8].setDecal(decal);

        decal = f.getInstance();
        terrain[18][8].setDecal(decal);

        decal = f.getInstance();
        terrain[10][19].setDecal(decal);

        decal = f.getInstance();
        terrain[12][19].setDecal(decal);

        decal = f.getInstance();
        terrain[14][19].setDecal(decal);

        decal = f.getInstance();
        terrain[16][19].setDecal(decal);

        decal = f.getInstance();
        terrain[18][19].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][16].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][15].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][14].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][13].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][12].setDecal(decal);

        decal = f.getInstance();
        decal.setRotation(90);
        terrain[20][11].setDecal(decal);

        setActivePawn(p);
    }

    @Override
    public void exitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        float a = (float)(Math.cos(GameEngine.getEngine().getSystemTime() * .002) * .15 + .5);

        BLUE_CHANGING.a = RED_CHANGING.a = GREEN_CHANGING.a = YELLOW_CHANGING.a = a;
    }

    public Terrain[][] getTerrainMap() {
        return terrain;
    }

    public int getTerrainMapSize() {
        return size;
    }

    public void setActivePawn(Pawn pawn) {
        this.activePawn = pawn;

        PathFinder<Terrain> pf = new PathFinder<>();

        HashMap<Terrain, Path<Terrain>> map = pf.findDistance(pawn.getParent(), 4);

        for (Terrain t : map.keySet()) {
            t.setHighlight(BLUE_CHANGING);
        }
    }

    public Pawn getActivePawn() {
        return activePawn;
    }
}
