package com.raven.breakingsands;

import com.raven.engine.database.GameData;

public class Armor {

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
}
