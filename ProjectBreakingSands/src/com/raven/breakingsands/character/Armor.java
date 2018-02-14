package com.raven.breakingsands.character;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDatable;

public class Armor implements GameDatable {

    private GameData gameData;
    private int resistance;
    private String name;

    public Armor(GameData gameData) {
        resistance = gameData.getInteger("resistance");
        name = gameData.getString("name");
        this.gameData = gameData;
    }

    public int getResistance() {
        return resistance;
    }

    public String getName() {
        return name;
    }

    @Override
    public GameData toGameData() {
        return gameData;
    }
}
