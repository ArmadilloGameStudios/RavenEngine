package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.raven.engine2d.database.GameData;

public class TerrainTrigger {
    public String effect;
    public String replaceTerrain;

    public TerrainTrigger(GameData gameData) {
        System.out.println(gameData);

        gameData.ifHas("effect", e -> effect = e.asString());

        gameData.ifHas("replace_terrain", r -> replaceTerrain = r.asString());
    }
}
