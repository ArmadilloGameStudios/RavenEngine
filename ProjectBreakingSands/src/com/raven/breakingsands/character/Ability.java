package com.raven.breakingsands.character;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;

public class Ability implements GameDatable {

    private GameData gameData;

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
        this.gameData = gameData;

        name = gameData.getString("name");

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
        gameData.ifHas("remain", c -> remain = c.asBoolean());
        gameData.ifHas("taunt", t -> taunt = t.asBoolean());
        gameData.ifHas("push_blast", p -> push_blast = p.asBoolean());
        gameData.ifHas("hook_pull", h -> hook_pull = h.asBoolean());
        gameData.ifHas("hack", h -> hack = h.asBoolean());
    }

    @Override
    public GameData toGameData() {
        return gameData;

//        HashMap<String, GameData> map = new HashMap<>();
//
//        map.put("name", new GameData(name));
//
//        switch (type) {
//            default:
//            case SELF:
//                map.put("type", new GameData("self"));
//                break;
//            case AURORA:
//                map.put("type", new GameData("aurora"));
//                break;
//            case TARGET:
//                map.put("type", new GameData("target"));
//                break;
//        }
//
//        switch (target) {
//            case ALL:
//                map.put("target", new GameData("all"));
//                break;
//            case SELF:
//                map.put("target", new GameData("self"));
//                break;
//            case ALLY:
//                map.put("target", new GameData("ally"));
//                break;
//            case ENEMY:
//                map.put("target", new GameData("enemy"));
//                break;
//        }
//
//        switch (style) {
//            case STRAIGHT:
//                map.put("style", new GameData("straight"));
//                break;
//            case SQUARE:
//                map.put("style", new GameData("square"));
//                break;
//            case DIAMOND:
//                map.put("style", new GameData("diamond"));
//                break;
//        }
//
//        map.put("replace", new GameData(replace));
//        map.put("upgrade", new GameData(upgrade));
//        map.put("size", new GameData(size));
//        map.put("damage", new GameData(damage));
//        map.put("hp", new GameData(hp));
//        map.put("shield", new GameData(shield));
//        map.put("movement", new GameData(movement));
//        map.put("resistance", new GameData(resistance));
//        map.put("turns", new GameData(turns));
//        map.put("uses", new GameData(uses));
//        map.put("remain", new GameData(remain));
//        map.put("taunt", new GameData(taunt));
//        map.put("push_blast", new GameData(push_blast));
//        map.put("hook_pull", new GameData(hook_pull));
//        map.put("hack", new GameData(hack));
//
//        return new GameData(map);
    }
}
