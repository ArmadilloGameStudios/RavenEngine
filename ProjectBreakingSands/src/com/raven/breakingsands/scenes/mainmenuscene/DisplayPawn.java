package com.raven.breakingsands.scenes.mainmenuscene;

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

public class DisplayPawn extends WorldObject<Scene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("pawn");

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    public DisplayPawn(Scene scene) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches("Player");
            }
        }).getString("model"));
    }
}
