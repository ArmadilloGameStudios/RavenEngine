package com.armadillogamestudios.saga.data.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.saga.data.SagaGameData;

public class RegionData implements GameDatable {

    private final GameData gameData;
    private final Vector2i center = new Vector2i();
    private final int id;
    private final String name;
    private final TerrainData terrain;

    public RegionData(GameData gameData) {

        id = gameData.getInteger("id");
        name = gameData.getString("name");

        terrain = SagaGameData.getSagaTerrainData(gameData.getString("terrain"));

        center.x = gameData.getData("center").getInteger("x");
        center.y = gameData.getData("center").getInteger("y");

        this.gameData = gameData;
    }

    @Override
    public GameData toGameData() {
        return gameData;
    }

    public int getID() {
        return id;
    }

    public Vector2i getCenter() {
        return center;
    }

    public String getName() {
        return name;
    }
}
