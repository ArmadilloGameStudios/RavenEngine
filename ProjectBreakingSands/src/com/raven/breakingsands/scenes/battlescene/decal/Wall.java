package com.raven.breakingsands.scenes.battlescene.decal;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Wall extends WorldObject<BattleScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("wall");

    public static GameDataList getDataList() {
        return dataList;
    }

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    // instance
    private GameData gameData;
    private String name = "";
    private boolean passable = true;

    public Wall(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        this.gameData = gameData;

        name = gameData.getString("name");
    }


    public String getName() {
        return name;
    }

    public boolean isPassable() {
        return passable;
    }

    @Override
    public float getZ() {
        return ZLayer.DECAL.getValue();
    }
}
