package com.raven.breakingsands;

import com.raven.engine.database.GameData;

public class Weapon {
    private GameData gameData;
    private int damage = 3;
    private String name;

    public Weapon(GameData gameData) {
        damage = gameData.getInteger("damage");
        name = gameData.getString("name");
    }

    public int getDamage() {
        return damage;
    }

    public String getName() {
        return name;
    }
}
