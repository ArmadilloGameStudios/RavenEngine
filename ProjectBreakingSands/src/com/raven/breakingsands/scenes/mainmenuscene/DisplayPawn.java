package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.ZLayer;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisplayPawn extends WorldObject<Scene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("pawn");

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    public DisplayPawn(Scene scene) {
        super(scene, dataList.getRandom(new Random()));
    }

    @Override
    public float getZ() {
        return ZLayer.PAWN.getValue();
    }
}
