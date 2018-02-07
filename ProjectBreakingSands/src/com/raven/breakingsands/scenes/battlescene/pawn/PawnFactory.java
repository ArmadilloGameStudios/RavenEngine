package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.engine.util.Factory;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;

public class PawnFactory extends Factory<Pawn> {
    private BattleScene scene;
    private String name = null;

    public PawnFactory(BattleScene scene) {
        this.scene = scene;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Pawn getInstance() {
        GameData gameData = Pawn.getDataList().queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {

                boolean found = true;

                if (name != null) {
                    found &= row.getString("name").equals(name);
                }

                return found;
            }
        });

        return new Pawn(scene, gameData);
    }

    public void clear() {
        name = null;
    }
}
