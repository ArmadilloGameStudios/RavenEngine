package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.Factory;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;

public class PawnFactory extends Factory<Pawn> {
    private BattleScene scene;
    private int team = 0;
    private String name = null;

    public PawnFactory(BattleScene scene) {
        this.scene = scene;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(int team) {
        this.team = team;
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

        return new Pawn(scene, team, gameData);
    }

    public void clear() {
        team = 0;
    }
}
