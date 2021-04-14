package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class Building implements GameDatable {

    private Type type;
    private Player owner;

    public Building(Type type, Player owner) {

        this.type = type;
        this.owner = owner;
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public String getSprite() {
        return "default impassable map tile.png";
    }

    public enum Type {
        Fort,
    }
}
