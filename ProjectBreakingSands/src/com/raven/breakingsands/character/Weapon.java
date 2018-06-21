package com.raven.breakingsands.character;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Weapon
        extends WorldObject<BattleScene, Pawn, WorldObject>
        implements GameDatable {

    private static GameDataList dataList = GameDatabase.all("weapon");

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            gameData.ifHas("sprite", gd ->
                    data.add(GameEngine.getEngine().getSpriteSheet(gd.asString())));
        }

        return data;
    }

    private GameData gameData;
    private int damage, piercing = 0, range, rangeMin, shots;
    private boolean directional;
    private RangeStyle style;
    private String name;
    private Effect effect;

    public Weapon(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");

        damage = gameData.getInteger("damage");

        gameData.ifHas("style",
                s -> {
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
                },
                () -> style = RangeStyle.DIAMOND);

        if (gameData.getData("range").isInteger()) {
            range = gameData.getInteger("range");
            rangeMin = 1;
        } else {
            List<GameData> ranges = gameData.getList("range");
            range = ranges.get(1).asInteger();
            rangeMin = ranges.get(0).asInteger();
        }

        if (gameData.has("directional")) {
            directional = gameData.getBoolean("directional");
        }

        gameData.ifHas("piercing", gd -> piercing = gd.asInteger());

        gameData.ifHas("shots", gd -> shots = gd.asInteger(), () -> shots = 1);

        if (gameData.has("effect")) {
            GameData gdEffect = gameData.getData("effect");

            if (gdEffect.isString()) {
                GameDatabase.all("effect").stream()
                        .filter(e -> e.getString("name").equals(gdEffect.asString()))
                        .findFirst()
                        .ifPresent(e -> effect = new Effect(scene, e));
            } else {
                effect = new Effect(scene, gdEffect);
            }
        }

        this.gameData = gameData;
    }

    public void runAttackAnimation(boolean directionUp) {
        if (getAnimationState() != null) {

            if (directional)
                if (directionUp)
                    getAnimationState().setAction("attack up start");
                else
                    getAnimationState().setAction("attack down start");
            else
                getAnimationState().setAction("attack start");

            getAnimationState().addActionFinishHandler(x -> {
                if (directional)
                    if (directionUp)
                        getAnimationState().setAction("attack up end");
                    else
                        getAnimationState().setAction("attack down end");
                else
                    getAnimationState().setAction("attack end");

                getAnimationState().addActionFinishHandler(r -> {
                    getAnimationState().setAction("idle");
//                    setVisibility(false);
                });
            });
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getPiercing() {
        return piercing;
    }

    public int getRange() {
        return range;
    }

    public int getRangeMin() {
        return rangeMin;
    }

    public int getShots() {
        return shots;
    }

    public RangeStyle getStyle() {
        return style;
    }

    public String getName() {
        return name;
    }

    public Effect getEffect() {
        return effect;
    }

    @Override
    public GameData toGameData() {
        return gameData;
    }

    @Override
    public float getZ() {
        return ZLayer.WEAPON.getValue();
    }

    public boolean getDirectional() {
        return directional;
    }

}
