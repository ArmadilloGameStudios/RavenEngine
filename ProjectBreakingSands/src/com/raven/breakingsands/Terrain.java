package com.raven.breakingsands;

import com.raven.breakingsands.scenes.Decal;
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

public class Terrain extends WorldObject {
    private static GameDataList dataList = GameDatabase.all("terrain");

    private int x, y;
    private Decal decal;

    public Terrain(Scene scene, int x, int y) {
        super(scene, dataList.getRandom().getString("model"));
        this.x = x;
        this.y = y;
    }

    public Terrain(Scene scene, String name, int x, int y) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches(name);
            }
        }).getString("model"));

        this.x = x;
        this.y = y;
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    public void setDecal(Decal decal) {
        if (this.decal != null) {
            removeChild(this.decal);
        }

        this.decal = decal;

        this.addChild(decal);
    }
}
