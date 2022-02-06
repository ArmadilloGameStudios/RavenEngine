package com.armadillogamestudios.saga.data.world;

import com.armadillogamestudios.engine2d.database.GameDatabase;

import java.util.HashMap;
import java.util.Map;

public class TerrainManager {

    private final Map<String, TerrainData> terrainMap = new HashMap<>();

    public TerrainData get(GameDatabase gameDatabase, String name) {

        if (terrainMap.containsKey(name)) {
            return terrainMap.get(name);
        } else {
            TerrainData terrain =  new TerrainData(gameDatabase.getTable("terrain").query("name", name));

            terrainMap.put(name, terrain);

            return terrain;
        }
    }
}
