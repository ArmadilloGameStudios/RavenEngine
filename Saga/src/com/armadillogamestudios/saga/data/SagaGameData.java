package com.armadillogamestudios.saga.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;

public class SagaGameData {

    private final GameDatabase gameDatabase;

    public SagaGameData(GameDatabase gameDatabase) {
        this.gameDatabase = gameDatabase;
    }

    public GameData getSagaWorldData() {
        return gameDatabase.getTable("world").query("name", "saga");
    }
}
