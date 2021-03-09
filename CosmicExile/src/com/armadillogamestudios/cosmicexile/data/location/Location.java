package com.armadillogamestudios.cosmicexile.data.location;

import com.armadillogamestudios.cosmicexile.game.CosmicExileGame;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class Location implements GameDatable {

    private final String name;
    private final long mapSeed;

    public Location(String name) {
        this.name = name;

        mapSeed = CosmicExileGame.RANDOM.nextLong();
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public long getMapSeed() {
        return mapSeed;
    }

    public String getName() {
        return name;
    }

    public String getMapSetting() {
        return "test";
    }
}
