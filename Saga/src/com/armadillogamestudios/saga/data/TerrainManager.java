package com.armadillogamestudios.saga.data;

import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.saga.scene.map.Terrain;

import java.util.HashMap;
import java.util.Map;

public class TerrainManager {

    private final Map<String, Terrain> terrainMap = new HashMap<>();

    public Terrain get(GameDatabase gameDatabase, String name) {

        if (terrainMap.containsKey(name)) {
            return terrainMap.get(name);
        } else {
            Terrain terrain =  new Terrain(gameDatabase.getTable("terrain").query("name", name));

            terrainMap.put(name, terrain);

            return terrain;
        }
    }
}
