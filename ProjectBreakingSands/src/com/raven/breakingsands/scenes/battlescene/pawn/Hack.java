package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.scene.Scene;

import java.util.HashMap;

public class Hack implements GameDatable {

    private int team;
    private int remainingTurns;
    private int selfDestruct;
    private Pawn hacker;

    public Hack(Pawn hacker, int team, int turns, int selfDestruct) {
        this.hacker = hacker;
        this.team = team;
        this.remainingTurns = turns;
        this.selfDestruct = selfDestruct;
    }

    public Hack(BattleScene scene, GameData data) {
        if (data.has("pawn")) {
            hacker = scene.getPawns().get(data.getInteger("pawn"));
        } else {
            hacker = scene.getPawns().get(0);
        }
        this.team = data.getInteger("team");
        this.remainingTurns = data.getInteger("remainingTurns");
        this.selfDestruct = data.getInteger("selfDestruct");
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        map.put("pawn", new GameData(hacker.getScene().getPawns().indexOf(hacker)));
        map.put("team", new GameData(team));
        map.put("remainingTurns", new GameData(remainingTurns));
        map.put("selfDestruct", new GameData(selfDestruct));

        return new GameData(map);
    }

    public int getTeam() {
        return team;
    }

    public int getRemainingTurns() {
        return remainingTurns;
    }

    public int getSelfDestruct() {
        return selfDestruct;
    }

    public void tick() {
        remainingTurns--;
    }

    public Pawn getHacker() {
        return hacker;
    }
}
