package com.raven.breakingsands.mission;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;

import java.util.HashMap;

public class Mission implements GameDatable {
    private String name;

    public Mission(String name) {
        this.name = name;
    }

    public Mission(GameData gameData) {
        name = gameData.getString("name");
    }

    public String getName() {
        return name;
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> data = new HashMap<>();

        data.put("name", new GameData(name));

        return new GameData(data);
    }
}
