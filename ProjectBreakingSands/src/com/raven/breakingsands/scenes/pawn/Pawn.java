package com.raven.breakingsands.scenes.pawn;

import com.raven.breakingsands.scenes.TestScene;
import com.raven.breakingsands.scenes.terrain.Terrain;
import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Scene;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends WorldObject<TestScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("pawn");

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
    private String name = "";

    public Pawn(TestScene scene, int team, GameData gameData) {
        super(scene, gameData.getString("model"));

        this.gameData = gameData;

        name = gameData.getString("name");
    }

    public String getName() {
        return name;
    }
}
