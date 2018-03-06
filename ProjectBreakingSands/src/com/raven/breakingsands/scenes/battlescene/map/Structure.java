package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Structure extends WorldObject<BattleScene, Map, WorldObject> {

    private int width = 3, height = 3;
    private int x, y;

    private int mapRotation = 0;

    private String name;

    private StructureEntrance[] entrances;

    private List<Terrain> terrainList = new ArrayList<>();

    public Structure(BattleScene scene, int x, int y) {
        this(scene,
                GameEngine.getEngine().getGameDatabase().getTable("structure").getRandom(),
                0, x, y);
    }

    public Structure(BattleScene scene, GameData gameData, int rotation, int x, int y) {
        super(scene);

        this.x = x;
        this.y = y;

        width = gameData.getInteger("width");
        height = gameData.getInteger("height");
        mapRotation = rotation;

        name = gameData.getString("name");

        GameDataList gdtList = gameData.getList("terrain");

        for (GameData gdt : gdtList) {
                Terrain t = new Terrain(scene, this, gdt);
                addChild(t);
                terrainList.add(t);
        }

        GameDataList gdcList = gameData.getList("entrance");

        entrances = new StructureEntrance[gdcList.size()];

        for (int i = 0; i < gdcList.size(); i++) {
            entrances[i] = new StructureEntrance(this, gdcList.get(i));
        }

        this.setX(x * 2);
        this.setZ(y * 2);
    }

    public StructureEntrance[] getEntrances() {
        return entrances;
    }

    public List<Terrain> getTerrainList() {
        return terrainList;
    }

    public int getMapX() {
        return x;
    }

    public int getMapY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMapRotation() {
        return mapRotation;
    }
}
