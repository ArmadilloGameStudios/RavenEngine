package com.armadillogamestudios.saga.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.saga.scene.map.Terrain;

public class SagaGameData {

    private static final TerrainManager terrainManager = new TerrainManager();
    private static GameDatabase gameDatabase;

    private SagaGameData() {

    }

    public static void init(GameDatabase gameDatabase) {
        SagaGameData.gameDatabase = gameDatabase;
    }

    public static GameData getSagaWorldData() {
        return gameDatabase.getTable("world").get(0);
    }

    public static Terrain getSagaTerrainData(String name) {
        return terrainManager.get(gameDatabase, name);
    }
}
