package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;

import java.util.HashMap;

public class Hack implements GameDatable {

    private int team;
    private int remainingTurns;
    private int selfDestruct;

    public Hack(int team, int turns, int selfDestruct) {
        this.team = team;
        this.remainingTurns = turns;
        this.selfDestruct = selfDestruct;
    }

    public Hack(GameData data) {
        this.team = data.getInteger("team");
        this.remainingTurns = data.getInteger("remainingTurns");
        this.selfDestruct = data.getInteger("selfDestruct");
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        map.put("team", new GameData(team));
        map.put("remainingTurns", new GameData(remainingTurns));
        map.put("selfDestruct", new GameData(selfDestruct));

        return new GameData(map);
    }

    public int getTeam() {
        return team;
    }

    public int getRemainingTurnsTurns() {
        return remainingTurns;
    }

    public int getSelfDestruct() {
        return selfDestruct;
    }

    public void tick() {
        remainingTurns--;
    }
}
