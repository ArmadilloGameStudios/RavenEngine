package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.mainmenuscene.ContinueButton;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;

public class StructureEntrance {

    private int side, location, length;

    private StructureEntrance connection;

    public StructureEntrance(Structure structure, GameData data) {
        side = data.getInteger("side");
        location = data.getInteger("location");
        length = data.getInteger("length");
    }

    public int getLength() {
        return length;
    }

    public int getSide() {
        return side;
    }

    public int getLocation() {
        return location;
    }
}
