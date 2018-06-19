package com.raven.breakingsands.character;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;

public class Ability {

    public Pawn owner;

    public enum Type {SELF, AURORA}

    public enum Target {ALL, SELF, ALLY, ENEMY}

    public String name;
    public Type type;
    public Target target;
    public String replace;

    public Integer size;
    public Integer hp, shield, movement, resistance;
    public boolean taunt = false;

    public Ability(GameData gameData) {
        name = gameData.getString("name");

        switch (gameData.getString("type")) {
            default:
            case "self":
                type = Type.SELF;
                break;
            case "aurora":
                type = Type.AURORA;
                break;
        }

        gameData.ifHas("target",
                t -> {
                    switch (t.asString()) {
                        case "all":
                            target = Target.ALL;
                            break;
                        case "self":
                            target = Target.SELF;
                            break;
                        case "ally":
                            target = Target.ALLY;
                            break;
                        case "enemy":
                            target = Target.ENEMY;
                            break;
                    }
                },
                t -> target = Target.ALL);

        gameData.ifHas("replace", r -> replace = r.asString());
        gameData.ifHas("size", s -> size = s.asInteger());
        gameData.ifHas("hp", h -> hp = h.asInteger());
        gameData.ifHas("shield", s -> shield = s.asInteger());
        gameData.ifHas("movement", m -> movement = m.asInteger());
        gameData.ifHas("resistance", r -> resistance = r.asInteger());
        gameData.ifHas("taunt", t -> taunt = t.asBoolean());
    }
}
