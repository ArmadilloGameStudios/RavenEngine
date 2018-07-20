package com.raven.breakingsands.character;

import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatable;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Ability implements GameDatable {

    private GameData gameData;

    public Pawn owner;

    public enum Type {SELF, AURORA, TARGET}

    public static class Target {
        static final public int ALL = 0b1111, SELF = 0b1000, ALLY = 0b0001, ENEMY = 0b0010, EMPTY = 0b0100;
    }

    public enum UseRegainType {TURN, LEVEL}

    public String name, upgrade;
    public Type type;
    public int target;
    public RangeStyle style;
    public UseRegainType useRegainType = UseRegainType.TURN;
    public String replace;

    public Integer size, damage, uses;
    public Integer remainingUses;
    public Integer hp, shield, movement, resistance;
    public boolean remain, passesPawn, passesWall, usedThisTurn,
            taunt, push_blast, hook_pull,
            hack, instant_hack, transferable, cure,
            blink, recall, recall_unit;

    public Ability bonusAbility;

    public Ability(GameData gameData) {
        this.gameData = new GameData(gameData);

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
                case "empty":
                    target = Target.EMPTY;
                    break;
            }
        });

        gameData.ifHas("use_regain_type", t -> {
            switch (t.asString()) {
                case "turn":
                    useRegainType = UseRegainType.TURN;
                    break;
                case "level":
                    useRegainType = UseRegainType.LEVEL;
                    break;
            }
        }, () -> useRegainType = UseRegainType.TURN);

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
        gameData.ifHas("passes_pawn", p -> passesPawn = p.asBoolean());
        gameData.ifHas("passes_wall", p -> passesWall = p.asBoolean());
        gameData.ifHas("transferable", t -> transferable = t.asBoolean());
        gameData.ifHas("upgrade", u -> upgrade = u.asString());
        gameData.ifHas("size", s -> size = s.asInteger());
        gameData.ifHas("damage", d -> damage = d.asInteger());
        gameData.ifHas("hp", h -> hp = h.asInteger());
        gameData.ifHas("shield", s -> shield = s.asInteger());
        gameData.ifHas("movement", m -> movement = m.asInteger());
        gameData.ifHas("resistance", r -> resistance = r.asInteger());

        if (gameData.has("remaining_uses")) {
            gameData.ifHas("remaining_uses", u -> remainingUses = u.asInteger());
            gameData.ifHas("uses", u -> uses = u.asInteger());
        } else {
            gameData.ifHas("uses", u -> remainingUses = uses = u.asInteger());
        }

        gameData.ifHas("used_this_turn", u -> usedThisTurn = u.asBoolean());
        gameData.ifHas("remain", c -> remain = c.asBoolean());
        gameData.ifHas("taunt", t -> taunt = t.asBoolean());
        gameData.ifHas("push_blast", p -> push_blast = p.asBoolean());
        gameData.ifHas("hook_pull", h -> hook_pull = h.asBoolean());
        gameData.ifHas("hack", h -> hack = h.asBoolean());
        gameData.ifHas("instant_hack", h -> instant_hack = h.asBoolean());
        gameData.ifHas("cure", h -> cure = h.asBoolean());
        gameData.ifHas("blink", h -> blink = h.asBoolean());
        gameData.ifHas("recall", h -> recall = h.asBoolean());
        gameData.ifHas("recall_unit", h -> recall_unit = h.asBoolean());
        gameData.ifHas("ability", h -> bonusAbility = new Ability(h));
    }

    @Override
    public GameData toGameData() {
        if (remainingUses != null) {
            gameData.asMap().put("remaining_uses", new GameData(remainingUses));
        }
        gameData.asMap().put("used_this_turn", new GameData(usedThisTurn));
//        gameData.asMap().put("usedThisTurn", new GameData(usedThisTurn));

//        if (size != null)
//            gameData.asMap().put("size", new GameData(size));
//        else gameData.asMap().remove("size");
//
//        if (damage != null)
//            gameData.asMap().put("damage", new GameData(damage));
//        else gameData.asMap().remove("damage");
//
//        if (turns != null)
//            gameData.asMap().put("turns", new GameData(turns));
//        else gameData.asMap().remove("turns");
//
//        if (size != null)
//            gameData.asMap().put("size", new GameData(size));

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

    @Override
    public String toString() {
        return gameData.toString();
    }
}
