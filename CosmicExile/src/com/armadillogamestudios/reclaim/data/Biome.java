package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class Biome implements GameDatable {

    private final String sprite;
    private final String name;

    public Biome(GameData gameData) {
        sprite = gameData.getString("sprite");
        name = gameData.getString("name");
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public String getSprite() {
        return sprite;
    }

    public String getName() {
        return name;
    }
}
