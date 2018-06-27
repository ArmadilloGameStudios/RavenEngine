package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.util.Factory;

import java.util.concurrent.atomic.AtomicBoolean;

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
                AtomicBoolean matches = new AtomicBoolean(false);

                propertyData.ifHas("type",
                        p -> matches.set(row.getString("name").matches(p.asString())),
                        () -> matches.set(true));

                return matches.get();
            }
        });

        return new Terrain(structure.getScene(), structure, terrainData, propertyData);
    }

    @Override
    public void clear() {
        propertyData = null;
    }
}
