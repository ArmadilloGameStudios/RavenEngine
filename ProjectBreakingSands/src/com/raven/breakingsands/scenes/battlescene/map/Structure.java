package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.Arrays;
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
                null,
                0, x, y);
    }

    public Structure(BattleScene scene, GameData gameData, GameData connected, int rotation, int x, int y) {
        super(scene);

        this.x = x;
        this.y = y;

        if (rotation % 2 == 0) {
            width = gameData.getInteger("width");
            height = gameData.getInteger("height");
        } else {
            height = gameData.getInteger("width");
            width = gameData.getInteger("height");
        }
        mapRotation = rotation;

        name = gameData.getString("name");

        GameDataList gdtList = gameData.getList("terrain");

        TerrainFactory tf = new TerrainFactory(this);

        for (GameData gdt : gdtList) {
            tf.setPropertyData(gdt);

            Terrain t = tf.getInstance();
            addChild(t);
            terrainList.add(t);
        }

        GameDataList gdcList = gameData.getList("entrance");

        entrances = new StructureEntrance[gdcList.size()];

        for (int i = 0; i < gdcList.size(); i++) {
            entrances[i] = new StructureEntrance(this, gdcList.get(i));
        }

        if (connected != null)
            Arrays.stream(entrances).filter(e ->
                    e.getLength() == connected.getInteger("length") &&
                            e.getLocation() == connected.getInteger("location") &&
                            e.getSide() == connected.getInteger("side"))
                    .forEach(e -> e.setConnected(true));

        this.setX(x * 2);
        this.setZ(y * 2);
    }

    public StructureEntrance[] getEntrances() {
        return entrances;
    }

    public List<Terrain> getTerrainList() {
        return terrainList;
    }

    public String getName() {
        return name;
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
