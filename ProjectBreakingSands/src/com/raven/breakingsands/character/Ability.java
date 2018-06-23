package com.raven.breakingsands.character;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;

public class Ability {

    public Pawn owner;

    public enum Type {SELF, AURORA, TARGET}

    public enum Target {ALL, SELF, ALLY, ENEMY}

    public String name, upgrade;
    public Type type;
    public Target target;
    public RangeStyle style;
    public String replace;

    public Integer size, damage, turns, uses;
    public int remainingUses;
    public Integer hp, shield, movement, resistance;
    public boolean taunt, push_blast, hook_pull, hack, remain;

    public Ability(GameData gameData) {
        name = gameData.getString("name");
        System.out.println(name);

        switch (gameData.getString("type")) {
            default:
            case "self":
                type = Type.SELF;
                break;
            case "aurora":
                type = Type.AURORA;
                break;
            case "target":
                type = Type.TARGET;
                break;
        }

        gameData.ifHas("target", t -> {
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
        }, () -> target = Target.ALL);

        gameData.ifHas("style", s -> {
            switch (s.asString()) {
                case "straight":
                    style = RangeStyle.STRAIGHT;
                    break;
                case "square":
                    style = RangeStyle.SQUARE;
                    break;
                case "diamond":
                    style = RangeStyle.DIAMOND;
                    break;
            }
        }, () -> style = RangeStyle.DIAMOND);


        gameData.ifHas("replace", r -> replace = r.asString());
        gameData.ifHas("upgrade", u -> upgrade = u.asString());
        gameData.ifHas("size", s -> size = s.asInteger());
        gameData.ifHas("damage", d -> damage = d.asInteger());
        gameData.ifHas("hp", h -> hp = h.asInteger());
        gameData.ifHas("shield", s -> shield = s.asInteger());
        gameData.ifHas("movement", m -> movement = m.asInteger());
        gameData.ifHas("resistance", r -> resistance = r.asInteger());
        gameData.ifHas("turns", t -> turns = t.asInteger());
        gameData.ifHas("uses", u -> remainingUses = uses = u.asInteger());
        gameData.ifHas("remain", c -> remain = c.asBoolean() );
        gameData.ifHas("taunt", t -> taunt = t.asBoolean());
        gameData.ifHas("push_blast", p -> push_blast = p.asBoolean());
        gameData.ifHas("hook_pull", h -> hook_pull = h.asBoolean());
        gameData.ifHas("hack", h -> hack = h.asBoolean());
    }
}
