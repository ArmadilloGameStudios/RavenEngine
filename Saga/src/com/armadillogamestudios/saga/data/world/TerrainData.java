package com.armadillogamestudios.saga.data.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class TerrainData {

    private final String name;

    public TerrainData(GameData gameData) {
        name = gameData.getString("name");
    }

    public String getName() {
        return name;
    }
}
