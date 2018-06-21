package com.raven.breakingsands.scenes.battlescene.pawn;

public class Hack {
    private int team;
    private int remainingTurns;
    private int selfDestruct;


    public Hack(int team, int turns, int selfDestruct) {
        this.team = team;
        this.remainingTurns = turns;
        this.selfDestruct = selfDestruct;
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
