package com.raven.breakingsands;

import com.raven.engine.database.GameData;

public class Weapon {
    private GameData gameData;
    private int damage, piercing = 0, range, accuracy;
    private String name;

    public Weapon(GameData gameData) {
        damage = gameData.getInteger("damage");
        range = gameData.getInteger("range");
        accuracy = gameData.getInteger("accuracy");
        if (gameData.has("piercing")) {
            piercing = gameData.getInteger("piercing");
        }
        name = gameData.getString("name");
        this.gameData = gameData;
    }

    public int getDamage() {
        return damage;
    }

    public int getPiercing() {
        return piercing;
    }

    public int getRange() {
        return range;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public String getName() {
        return name;
    }
}
