package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Armor;
import com.raven.breakingsands.character.Character;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.graphics2d.sprite.handler.ActionFinishHandler;
import com.raven.engine2d.worldobject.WorldObject;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends WorldObject<BattleScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("pawn");

    public static GameDataList getDataList() {
        return dataList;
    }

    public static List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    // instance
    private Weapon weapon;
    private Armor armor;
    private String name = "";
    private int team, hitPoints, remainingHitPoints, totalMovement, remainingMovement, evasion, totalAttacks = 1, remainingAttacks;

    public Pawn(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");
        team = gameData.getInteger("team");
        remainingHitPoints = hitPoints = gameData.getInteger("hp");
        totalMovement = gameData.getInteger("movement");
        evasion = gameData.getInteger("evasion");

        // weapon
        if (gameData.has("weapon")) {
            GameData gdWeapon = gameData.getData("weapon");

            if (gdWeapon.isString()) {
                GameDatabase.all("weapon").stream()
                        .filter(w -> w.getString("name").equals(gdWeapon.asString()))
                        .findFirst()
                        .ifPresent(w -> weapon = new Weapon(scene, w));
            } else {
                weapon = new Weapon(scene, gdWeapon);
            }
        } else {
            weapon = new Weapon(scene, GameDatabase.all("weapon").getRandom());
        }

        if (weapon != null) {
            addChild(weapon);
        }

        // armor
        if (gameData.has("armor")) {
            GameData gdArmor = gameData.getData("armor");

            if (gdArmor.isString()) {
                GameDatabase.all("armor").stream()
                        .filter(a -> a.getString("name").equals(gdArmor.asString()))
                        .findFirst()
                        .ifPresent(a -> armor = new Armor(a));
            } else {
                armor = new Armor(gdArmor);
            }
        } else {
            armor = new Armor(GameDatabase.all("armor").getRandom());
        }
    }

    public Pawn(BattleScene scene, Character character) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").equals("Player");
            }
        }));

        name = character.getTitle() + " " + character.getName();
        team = 0;
        hitPoints = character.getHitPoints();
        remainingHitPoints = character.getHitPoints();
        totalMovement = character.getMovement();
        evasion = character.getEvasion();

        weapon = new Weapon(scene, GameDatabase.all("weapon").getRandom());
        addChild(weapon);

        armor = new Armor(GameDatabase.all("armor").getRandom());
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        return team;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getRemainingHitPoints() {
        return remainingHitPoints;
    }

    public int getTotalMovement() {
        return totalMovement;
    }

    public int getRemainingMovement() {
        return remainingMovement;
    }

    public int getRemainingAttacks() {
        return remainingAttacks;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public void ready() {
        remainingMovement = totalMovement;
        remainingAttacks = totalAttacks;
    }

    public void move(int amount) {
        remainingMovement = Math.max(remainingMovement - amount, 0);
    }

    public void runAttackAnimation(Pawn target, ActionFinishHandler onAttackDone) {
        boolean directional = getWeapon().getDirectional();
        boolean directionUp = target.getParent().getMapX() < getParent().getMapX() ||
                        target.getParent().getMapY() > getParent().getMapY();

        weapon.playClip("attack");

        setFlip(target.getParent().getMapY() > getParent().getMapY() ||
                target.getParent().getMapX() > getParent().getMapX());

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

            getAnimationState().addActionFinishHandler(a -> {
                getAnimationState().setAction("idle");
            });


            Effect effect = weapon.getEffect();
            if (effect != null) {
                target.addChild(effect);
                effect.getAnimationState().addActionFinishHandler(a -> target.removeChild(effect));
            }

            getAnimationState().addActionFinishHandler(onAttackDone);
        });

        weapon.runAttackAnimation(directionUp);

    }

    public void attack(Pawn pawn) {
        remainingAttacks = Math.max(totalAttacks - 1, 0);

        // Check if hit
        int target = pawn.getWeapon().getAccuracy();
        int totalRange = pawn.evasion + target;
        int rolled = getScene().getRandom().nextInt(totalRange);

//        System.out.println("Accuracy: " + target);
//        System.out.println("Evasion: " + pawn.evasion);
//        System.out.println("Total Range: " + totalRange);
//        System.out.println("Rolled: " + rolled);

        if (rolled < target) {
            int remainingResistance = Math.max(pawn.armor.getResistance() - weapon.getPiercing(), 0);
            int dealtDamage = Math.max(weapon.getDamage() - remainingResistance, 0);
            pawn.remainingHitPoints = Math.max(pawn.remainingHitPoints - dealtDamage, 0);

            if (pawn.remainingHitPoints == 0) {
                pawn.die();
            }

            pawn.getParent().updateText();
        } else {
//            System.out.println("MISS");
        }
    }

    public void die() {
        System.out.println("DIE");
        getParent().removePawn();
        getScene().removePawn(this);
    }

    @Override
    public float getZ() {
        return ZLayer.PAWN.getValue();
    }

    public void setFlip(boolean flip) {
        getAnimationState().setFlip(flip);

        SpriteAnimationState weaponState = getWeapon().getAnimationState();
        if (weaponState != null) {
            weaponState.setFlip(flip);
        }
    }

    public int getEvasion() {
        return evasion;
    }
}
