package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Ability;
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
import com.raven.engine2d.worldobject.WorldTextObject;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.Hashtable;
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
    private String name = "", charClass = "recruit";
    private int level = 0, team,
            hitPoints, remainingHitPoints, bonusHp,
            totalShield, remainingShield, bonusShield,
            totalMovement, remainingMovement,
            resistance, totalAttacks = 1, remainingAttacks;
    private boolean ready = true;
    private List<Ability> abilities = new ArrayList<>();
    private List<Ability> abilityAffects = new ArrayList<>();

    public Pawn(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");
        team = gameData.getInteger("team");
        remainingHitPoints = hitPoints = gameData.getInteger("hp");
        gameData.ifHas("shield",
                gd -> remainingShield = totalShield = gd.asInteger());
        totalMovement = gameData.getInteger("movement");

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

    public int getBonusHp() {
        return bonusHp;
    }

    public int getTotalShield() {
        return totalShield;
    }

    public int getRemainingShield() {
        return remainingShield;
    }

    public int getBonusShield() {
        return bonusShield;
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

    public int getResistance() {
        return resistance;
    }

    public String getCharacterClass() {
        return charClass;
    }

    public void setCharacterClass(GameData newCharClass) {
        this.name = this.charClass = newCharClass.getString("name");

        GameData bonus = newCharClass.getData("bonus");

        bonus.ifHas("hp", gd -> {
            hitPoints += gd.asInteger();
            remainingHitPoints += gd.asInteger();
        });
        bonus.ifHas("shield", gd -> {
            totalShield += gd.asInteger();
            remainingShield += gd.asInteger();
        });
        bonus.ifHas("resistance", gd -> resistance += gd.asInteger());
        bonus.ifHas("movement", gd -> {
            totalMovement += gd.asInteger();
            remainingMovement += gd.asInteger();
        });
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);

        if (ability.type == Ability.Type.SELF) {
            if (ability.hp != null) {
                hitPoints += ability.hp;
                remainingHitPoints += ability.hp;
            }
            if (ability.shield != null) {
                totalShield += ability.shield;
                remainingShield += ability.shield;
            }
            if (ability.movement != null) {
                totalMovement += ability.movement;
                remainingMovement += ability.movement;
            }
            if (ability.resistance != null) {
                resistance += ability.resistance;
            }
        }

        getParent().setPawn(this); // Shitty way of making sure the aurora effect is there
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbilityAffect(Ability a) {
        if (a.target == Ability.Target.ALL ||
                (a.target == Ability.Target.ALLY && team == 0) ||
                (a.target == Ability.Target.ENEMY && team == 1)) {

            abilityAffects.add(a);

            if (a.hp != null)
                this.bonusHp += a.hp;
            if (a.shield != null)
                this.bonusShield += a.shield;
            if (a.resistance != null)
                this.resistance += a.resistance;

            getParent().updateText();
        }
    }

    public void removeAbilityAffect(Ability a) {
        if (abilityAffects.remove(a)) {

            if (a.hp != null)
                this.bonusHp -= a.hp;
            if (a.shield != null)
                this.bonusShield -= a.shield;
            if (a.resistance != null)
                this.resistance -= a.resistance;

            getParent().updateText();
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int lvl) {
        level = lvl;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;

        if (weapon != null) {
            addChild(weapon);
        }
    }

    public void ready() {
        ready = true;
        remainingMovement = totalMovement;
        remainingAttacks = totalAttacks;
    }

    public void move(int amount) {
        remainingMovement = Math.max(remainingMovement - amount, 0);

        if (remainingMovement == 0 && remainingAttacks == 0) {
            ready = false;
        }
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

            attack(target);

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

        if (remainingAttacks == 0) {
            ready = false;
        }

        int remainingResistance = Math.max(pawn.resistance - weapon.getPiercing(), 0);
        int dealtDamage = Math.max(weapon.getDamage() - remainingResistance, 0) * weapon.getShots();

        if (dealtDamage <= 0) {
            dealtDamage = 1;
        }

        int rolloverBonusShieldDamage = dealtDamage;
        if (pawn.bonusShield > 0) {
            rolloverBonusShieldDamage = -Math.min(pawn.bonusShield - dealtDamage, 0);
            pawn.bonusShield = Math.max(pawn.bonusShield - dealtDamage, 0);
        }

        int rolloverShieldDamage = -Math.min(pawn.remainingShield - rolloverBonusShieldDamage, 0);
        pawn.remainingShield = Math.max(pawn.remainingShield - rolloverBonusShieldDamage, 0);

        int rolloverBonusHp = rolloverShieldDamage;
        if (pawn.bonusHp > 0) {
            rolloverBonusHp = -Math.min(pawn.bonusHp - rolloverShieldDamage, 0);
            pawn.bonusHp = Math.max(pawn.bonusHp - rolloverShieldDamage, 0);
        }
        pawn.remainingHitPoints = Math.max(pawn.remainingHitPoints - rolloverBonusHp, 0);

        if (pawn.remainingHitPoints == 0) {
            pawn.die();
        }

        pawn.getParent().updateText();

        pawn.showDamage("-" + Integer.toString(dealtDamage));
    }

    private void showDamage(String damage) {
        getScene().showDamage(this, damage);
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

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean checkReady(boolean noOptions) {
        if (remainingAttacks == 0) {
            ready = false;
        } else if (remainingMovement == 0 && noOptions) {
            ready = false;
        } else {
            ready = true;
        }

        return ready;
    }

    public void setReadyIsMoved(boolean readyIsMoved) {
        if (remainingMovement != totalMovement || remainingAttacks != totalAttacks)
            this.ready = readyIsMoved;
    }
}
