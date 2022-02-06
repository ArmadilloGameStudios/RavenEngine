package com.armadillogamestudios.saga.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.saga.data.world.RegionData;
import com.armadillogamestudios.saga.data.world.TerrainData;
import com.armadillogamestudios.saga.data.world.TerrainManager;
import com.armadillogamestudios.saga.data.world.WorldData;

public class SagaGameData {

    private static final TerrainManager terrainManager = new TerrainManager();
    private static WorldData worldData;
    private static GameDatabase gameDatabase;

    private SagaGameData() {

    }

    public static void init(GameDatabase gameDatabase) {
        SagaGameData.gameDatabase = gameDatabase;
    }

    public static WorldData getSagaWorldData() {
        if (worldData == null) {
            worldData = new WorldData(gameDatabase.getTable("world").get(0));
        }

        return worldData;
    }

    public static RegionData getRegionDataByID(int id) {
        return getSagaWorldData().getRegionByID(id);
    }


    public static TerrainData getSagaTerrainData(String name) {
        return terrainManager.get(gameDatabase, name);
    }
}
