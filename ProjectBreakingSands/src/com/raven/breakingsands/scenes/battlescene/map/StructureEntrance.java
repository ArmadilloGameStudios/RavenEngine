package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.mainmenuscene.ContinueButton;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;

public class StructureEntrance {

    private int side, location, length;
    private String name;
    private boolean connected = false;

    public StructureEntrance(Structure structure, GameData data) {
        side = data.getInteger("side");
        location = data.getInteger("location");
        length = data.getInteger("length");
        name = data.getString("name");
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

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }

    public String getName() {
        return name;
    }
}
