package com.raven.sunny;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookedbird on 11/13/17.
 */
public class Tree extends WorldObject {
    private static GameDataList dataList = GameDatabase.queryAll("plants", "type", "tree");

    public Tree() {
        super(dataList.getRandom().getString("model"));
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }
}
