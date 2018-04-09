package com.raven.breakingsands.scenes.battlescene.decal;

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

public class Decal extends WorldObject<BattleScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("decal");

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
    private String description = "";
    private boolean passable = true;

    public Decal(BattleScene scene, GameData gameData) {
        super(scene, gameData);

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
