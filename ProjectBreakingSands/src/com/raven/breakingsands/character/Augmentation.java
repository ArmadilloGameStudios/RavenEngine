package com.raven.breakingsands.character;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;

public class Augmentation {
    private String name = "Cat";

    private int movement, evasion, hp, resistance;

    public Augmentation() {
        this(GameEngine.getEngine().getGameDatabase().getTable("augmentation").getRandom());
    }

    public Augmentation(GameData gameData) {
        name = gameData.getString("name");
        if (gameData.has("movement"))
            movement = gameData.getInteger("movement");
        if (gameData.has("evasion"))
            evasion = gameData.getInteger("evasion");
        if (gameData.has("hp"))
            hp = gameData.getInteger("hp");
        if (gameData.has("resistance"))
            resistance = gameData.getInteger("resistance");
    }

    public String getName() {
        return name;
    }

    public int getMovement() {
        return movement;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getHP() {
        return hp;
    }

    public int getResistance() {
        return resistance;
    }
}
