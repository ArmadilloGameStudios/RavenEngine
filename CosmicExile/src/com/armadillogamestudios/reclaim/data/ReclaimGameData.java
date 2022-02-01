package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;

public class ReclaimGameData {

    private final GameDatabase gameDatabase;

    public ReclaimGameData(GameDatabase gameDatabase) {
        this.gameDatabase = gameDatabase;
    }

    public GameData getReclaimWorldData() {
        return gameDatabase.getTable("world").query("name", "reclaim");
    }
}
