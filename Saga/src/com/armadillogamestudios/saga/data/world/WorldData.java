package com.armadillogamestudios.saga.data.world;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.HashMap;
import java.util.Map;

public class WorldData implements GameDatable {

    private final Map<Integer, RegionData> regionDataIDMap = new HashMap<>();

    private final GameData gameData;

    public WorldData(GameData gameData) {

        this.gameData = gameData;

        GameDataList regions = gameData.getData("map").getList("regions");

        regions.forEach(gd -> {
            regionDataIDMap.put(gd.getInteger("id"), new RegionData(gd));
        });
    }

    @Override
    public GameData toGameData() {
        return gameData;
    }

    public RegionData getRegionByID(int id) {
        return regionDataIDMap.get(id);
    }
}
