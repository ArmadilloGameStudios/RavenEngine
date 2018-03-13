package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.mainmenuscene.ContinueButton;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;

public class StructureEntrance {

    private int side, location, length;
    private String name;

    private StructureEntrance connection;
    private Structure structure;

    public StructureEntrance(Structure structure, GameData data) {
        this.structure = structure;

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

    public void setConnected(StructureEntrance connection) {
        this.connection = connection;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public String getName() {
        return name;
    }

    public StructureEntrance getConnection() {
        return connection;
    }

    public Structure getStructure() {
        return structure;
    }
}
