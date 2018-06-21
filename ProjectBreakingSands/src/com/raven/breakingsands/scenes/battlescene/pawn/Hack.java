package com.raven.breakingsands.scenes.battlescene.pawn;

public class Hack {
    private int team;
    private int remaingingTurns;

    public Hack(int team, int turns) {
        this.team = team;
        this.remaingingTurns = turns;
    }

    public int getTeam() {
        return team;
    }

    public int getRemaingingTurns() {
        return remaingingTurns;
    }

    public void tick() {
        remaingingTurns--;
    }
}
