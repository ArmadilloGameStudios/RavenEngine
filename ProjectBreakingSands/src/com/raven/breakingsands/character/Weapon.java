package com.raven.breakingsands.character;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.worldobject.WorldObject;

public class Weapon
        extends WorldObject<BattleScene, Pawn, WorldObject>
        implements GameDatable {
    private GameData gameData;
    private int damage, piercing = 0, range, accuracy;
    private String name;
    private Effect effect;

    public Weapon(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");

        damage = gameData.getInteger("damage");
        range = gameData.getInteger("range");
        accuracy = gameData.getInteger("accuracy");

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

    public int getDamage() {
        return damage;
    }

    public int getPiercing() {
        return piercing;
    }

    public int getRange() {
        return range;
    }

    public int getAccuracy() {
        return accuracy;
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
        return 0f;
    }
}
