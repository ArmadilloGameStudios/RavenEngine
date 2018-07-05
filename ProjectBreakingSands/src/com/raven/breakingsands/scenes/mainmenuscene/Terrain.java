package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.map.Map;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Terrain extends WorldObject<MainMenuScene, Map, WorldObject> {

    private static GameDataList dataList = GameDatabase.all("terrain");

    public static List<SpriteSheet> getModelData() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("model")));
        }

        return data;
    }

    public Terrain(MainMenuScene scene) {
        super(scene, dataList.queryRandom(new Random(), new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches("sand");
            }
        }));
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return ZLayer.TERRAIN.getValue();
    }
}
