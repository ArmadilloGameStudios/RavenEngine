package com.raven.sunny;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

/**
 * Created by cookedbird on 11/13/17.
 */
public class Tree extends WorldObject {
    private static GameDataList dataList = GameDatabase.queryAll("plants", "name", "palm tree");

    public Tree(Parentable parent) {
        super(parent, dataList.getRandom().getString("model"));
    }
}
