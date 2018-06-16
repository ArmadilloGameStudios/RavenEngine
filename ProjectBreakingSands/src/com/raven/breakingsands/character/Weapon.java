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
            if (gameData.has("sprite"))
                data.add(GameEngine.getEngine().getSpriteSheet(
                        gameData.getString("sprite")));
        }

        return data;
    }

    private GameData gameData;
    private int damage, piercing = 0, range, rangeMin;
    private boolean directional;
    private String name;
    private Effect effect;

    public Weapon(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");

        damage = gameData.getInteger("damage");
        range = gameData.getInteger("range");

        if (gameData.has("directional")) {
            directional = gameData.getBoolean("directional");
        }

        if (gameData.has("piercing")) {
            piercing = gameData.getInteger("piercing");
        }

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

//            setVisibility(true);

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
