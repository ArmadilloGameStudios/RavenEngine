package com.raven.breakingsands.scenes.pawn;

import com.raven.breakingsands.Factory;
import com.raven.breakingsands.scenes.TestScene;
import com.raven.breakingsands.scenes.decal.Decal;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.scene.Scene;

public class PawnFactory extends Factory<Pawn> {
    private TestScene scene;
    private int team = 0;

    public PawnFactory(TestScene scene) {
        this.scene = scene;
    }

    public Pawn getInstance() {
        GameData gameData = Pawn.getDataList().queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return true;
            }
        });

        return new Pawn(scene, team, gameData);
    }

    public void clear() {
        team = 0;
    }
}
