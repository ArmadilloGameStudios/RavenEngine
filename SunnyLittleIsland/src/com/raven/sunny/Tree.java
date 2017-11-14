package com.raven.sunny;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

/**
 * Created by cookedbird on 11/13/17.
 */
public class Tree extends WorldObject {
    private static String src = GameEngine.getEngine().getGameDatabase()
            .getTable("plants")
            .getFirst(new GameDataQuery() {
                @Override
                public boolean matches(GameData row) {
                    return row.getData("name").getString().equals("palm tree");
                }
            }).getData("model").getString();

    public Tree(Parentable parent) {
        super(parent, src);
    }
}
