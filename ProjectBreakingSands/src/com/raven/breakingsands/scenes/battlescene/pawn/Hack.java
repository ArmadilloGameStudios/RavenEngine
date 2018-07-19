package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.scene.Scene;

import java.util.HashMap;

public class Hack implements GameDatable {

    private int team;
    private int selfDestruct;
    private boolean instant;
    private Pawn hacker;

    public Hack(Pawn hacker, int team, int selfDestruct, boolean instant) {
        this.hacker = hacker;
        this.team = team;
        this.selfDestruct = selfDestruct;
        this.instant = instant;
    }

    public Hack(BattleScene scene, GameData data) {
        if (data.has("pawn") && data.getInteger("pawn") != -1) {
            hacker = scene.getPawns().get(data.getInteger("pawn"));
        } else {
            hacker = scene.getPawns().get(0);
        }
        this.team = data.getInteger("team");
        this.selfDestruct = data.getInteger("selfDestruct");
        this.instant = data.getBoolean("instant");
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        map.put("pawn", new GameData(hacker.getScene().getPawns().indexOf(hacker)));
        map.put("team", new GameData(team));
        map.put("selfDestruct", new GameData(selfDestruct));
        map.put("instant", new GameData(instant));

        return new GameData(map);
    }

    public int getTeam() {
        return team;
    }

    public int getSelfDestruct() {
        return selfDestruct;
    }

    public Pawn getHacker() {
        return hacker;
    }

    public boolean isInstant() {
        return instant;
    }
}
