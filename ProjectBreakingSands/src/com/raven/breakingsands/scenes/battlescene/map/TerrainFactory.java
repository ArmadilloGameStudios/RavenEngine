package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.util.Factory;

import java.sql.Struct;

public class TerrainFactory extends Factory<Terrain> {

    private static GameDataList dataList = GameDatabase.all("terrain");

    private Structure structure;
    private GameData propertyData;

    public TerrainFactory(Structure structure) {
        this.structure = structure;
    }

    public void setPropertyData(GameData data) {
        propertyData = data;
    }

    @Override
    public Terrain getInstance() {
        GameData terrainData = dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").matches(propertyData.getString("type"));
            }
        });

        return new Terrain(structure.getScene(), structure, terrainData, propertyData);
    }

    @Override
    public void clear() {
        propertyData = null;
    }
}