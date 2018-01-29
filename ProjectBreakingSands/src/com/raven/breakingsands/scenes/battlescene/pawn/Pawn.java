package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.terrain.Terrain;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends WorldObject<BattleScene, Terrain, WorldObject> {
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
    private int team;

    public Pawn(BattleScene scene, int team, GameData gameData) {
        super(scene, gameData.getString("model"));

        this.gameData = gameData;

        name = gameData.getString("name");
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }
}
