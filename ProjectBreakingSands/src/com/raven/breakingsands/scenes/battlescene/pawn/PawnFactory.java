package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.Factory;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;

public class PawnFactory extends Factory<Pawn> {
    private BattleScene scene;
    private int team = 0;

    public PawnFactory(BattleScene scene) {
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
