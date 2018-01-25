package com.raven.breakingsands.scenes.decal;

import com.raven.breakingsands.scenes.BattleScene;
import com.raven.breakingsands.scenes.terrain.Terrain;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Decal extends WorldObject<BattleScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("decal");

    public static GameDataList getDataList() {
        return dataList;
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    // instance
    private GameData gameData;
    private String description = "";
    private boolean passable = true;

    public Decal(BattleScene scene, GameData gameData) {
        super(scene, gameData.getString("model"));

        this.gameData = gameData;

        description = gameData.getString("description");
        passable = gameData.getBoolean("passable");
    }


    public String getDescription() {
        return description;
    }

    public boolean isPassable() {
        return passable;
    }
}
