package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.engine2d.util.Factory;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataQuery;

public class PawnFactory extends Factory<Pawn> {
    private BattleScene scene;
    private String name = null;
    private Integer team = null;

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
        GameData gameData = Pawn.getDataList(scene).queryRandom(scene.getRandom(), new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {

                boolean found = true;

                if (name != null) {
                    found &= row.getString("name").equals(name);
                }
                if (team != null) {
                    found &= row.getInteger("team") == team;
                }

                return found;
            }
        });

        return new Pawn(scene, gameData);
    }

    public void clear() {
        name = null;
        team = null;
    }
}
