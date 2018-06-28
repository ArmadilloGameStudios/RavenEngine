package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.graphics2d.sprite.handler.ActionFinishHandler;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.WorldObject;

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
    private String name = "", charClass = "recruit";
    private int level = 0, team,
            hitPoints, remainingHitPoints, bonusHp,
            totalShield, remainingShield, bonusShield,
            totalMovement, remainingMovement,
            resistance, totalAttacks = 1, remainingAttacks,
            xp, xpGain;
    private Hack hack;
    private boolean unmoved = true;
    private boolean ready = true;
    private List<Ability> abilities = new ArrayList<>();
    private List<Ability> abilityAffects = new ArrayList<>();

    private PawnMessage pawnMessage;
    private float messageShowTime;

    public Pawn(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        name = gameData.getString("name");
        team = gameData.getInteger("team");
        remainingHitPoints = hitPoints = gameData.getInteger("hp");
        gameData.ifHas("shield",
                gd -> remainingShield = totalShield = gd.asInteger());
        totalMovement = gameData.getInteger("movement");
        gameData.ifHas("xp_gain", x -> xpGain = x.asInteger());

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
            weapon = new Weapon(scene, GameDatabase.all("weapon").getRandom(scene.getRandom()));
        }

        if (weapon != null) {
            addChild(weapon);
        }

        pawnMessage = new PawnMessage(scene);
        Vector2f pos = pawnMessage.getWorldPosition();
        pos.x -= .9;
        pos.y += 1.3;
        pawnMessage.setPosition(pos);
        addChild(pawnMessage);
    }

    public String getName() {
        return name;
    }

    public int getTeam() {
        if (hack == null)
            return team;
        else
            return hack.getTeam();
    }

    public void setTeam(int i) {
        team = i;
    }

    public void hack(Hack hack) {
        this.hack = hack;
    }

    public Hack getHack() {
        return hack;
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

    public boolean canMove() {
        boolean canMove = true;

        canMove &= remainingMovement > 0;

        canMove &= remainingAttacks == totalAttacks;

        for (Ability a : abilities) {
            canMove &= a.uses == null || (a.remainingUses == a.uses || a.remain);
        }

        return canMove;
    }

    public boolean canLevel() {
        return xp > (level * (level + 1) + 1) * 100;
    }

    public boolean canAttack() {
        boolean canMove = true;

        canMove &= remainingAttacks == totalAttacks;

        for (Ability a : abilities) {
            canMove &= a.uses == null || (a.remainingUses == a.uses || a.remain);
        }

        return canMove;
    }

    public boolean canAbility(Ability ability) {
        boolean canMove = true;

        canMove &= remainingAttacks == totalAttacks;

        for (Ability a : abilities) {
            if (a == ability)
                canMove &= a.uses == null || (a.remainingUses > 0);
            else
                canMove &= a.uses == null || (a.remainingUses == a.uses || a.remain);

        }

        return canMove;
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

        // add ability to the pawn if it is part of the class upgrade
        bonus.ifHas("ability", a -> {
            GameDatabase.all("classes").stream()
                    .filter(c -> c.getString("name").equals(this.charClass))
                    .map(c -> c.getList("abilities"))
                    .findFirst()
                    .map(aa -> aa.stream().filter(ab -> ab.getString("name").equals(a.asString())).findFirst())
                    .ifPresent(x -> x.ifPresent(ability -> addAbility(new Ability(ability))));
        });
    }

    public void addAbility(Ability ability) {
        ability.owner = this;

        if (ability.replace != null) {
            abilities.stream()
                    .filter(a -> a.name.equals(ability.replace))
                    .findFirst()
                    .ifPresent(this::removeAbility);
        }
        abilities.add(ability);

        if (ability.type == Ability.Type.SELF) {
            if (ability.upgrade == null) {
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
            } else { // upgrade existing ability
                abilities.stream()
                        .filter(a -> a.name.equals(ability.upgrade))
                        .forEach(a -> {
                            if (ability.size != null) {
                                if (a.size == null)
                                    a.size = ability.size;
                                else
                                    a.size += ability.size;
                            }
                            if (ability.damage != null) {
                                if (a.damage == null)
                                    a.damage = ability.damage;
                                else
                                    a.damage += ability.damage;
                            }
                            if (ability.turns != null) {
                                if (a.turns == null)
                                    a.turns = ability.turns;
                                else
                                    a.turns += ability.turns;
                            }
                            if (ability.uses != null) {
                                if (a.uses == null) {
                                    a.uses = ability.uses;
                                    a.remainingUses = ability.uses;
                                } else {
                                    a.uses += ability.uses;
                                    a.remainingUses += ability.uses;
                                }
                            }
                            a.remain |= ability.remain;
                        });
            }
        }

        getParent().setPawn(this); // Shitty way of making sure the aurora effect is there
    }

    public void removeAbility(Ability a) {
        abilities.remove(a);
        getParent().removePawnAbility(a);
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbilityAffect(Ability a) {
        if (a.target == Ability.Target.ALL ||
                (a.target == Ability.Target.ALLY && getTeam() == 0) ||
                (a.target == Ability.Target.ENEMY && getTeam() == 1)) {

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

    public List<Ability> getAbilityAffects() {
        return abilityAffects;
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
        setReady(true);
        remainingMovement = totalMovement;
        remainingAttacks = totalAttacks;

        abilities.forEach(a -> {
            if (a.uses != null) a.remainingUses = a.uses;
        });
    }

    public void move(int amount) {
        remainingMovement = Math.max(remainingMovement - amount, 0);

        setUnmoved(false);
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
                getAnimationState().setActionIdle();
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
        reduceAttacks();

        int remainingResistance = Math.max(pawn.resistance - weapon.getPiercing(), 0);
        int dealtDamage = Math.max(weapon.getDamage() - remainingResistance, 0) * weapon.getShots();

        setUnmoved(false);

        if (pawn.damage(dealtDamage) && getTeam() == 0) {
            xp += pawn.xpGain;
            showMessage("+" + Integer.toString(pawn.xpGain) + "xp");

            getScene().getPawns().stream()
                    .filter(p -> p.getTeam() == 0 && p != this)
                    .forEach(p -> {
                        p.xp += pawn.xpGain / 3;
                        p.showMessage("+" + Integer.toString(pawn.xpGain / 3) + "xp");
                    });

        }
    }

    public void reduceAttacks() {
        remainingAttacks = Math.max(totalAttacks - 1, 0);

        if (remainingAttacks == 0) {
            setUnmoved(false);
        }
    }

    public boolean damage(int dealtDamage) {
        if (dealtDamage <= 0) {
            dealtDamage = 1;
        }

        int rolloverBonusShieldDamage = dealtDamage;
        if (this.bonusShield > 0) {
            rolloverBonusShieldDamage = -Math.min(this.bonusShield - dealtDamage, 0);
            this.bonusShield = Math.max(this.bonusShield - dealtDamage, 0);
        }

        int rolloverShieldDamage = -Math.min(this.remainingShield - rolloverBonusShieldDamage, 0);
        this.remainingShield = Math.max(this.remainingShield - rolloverBonusShieldDamage, 0);

        int rolloverBonusHp = rolloverShieldDamage;
        if (this.bonusHp > 0) {
            rolloverBonusHp = -Math.min(this.bonusHp - rolloverShieldDamage, 0);
            this.bonusHp = Math.max(this.bonusHp - rolloverShieldDamage, 0);
        }
        this.remainingHitPoints = Math.max(this.remainingHitPoints - rolloverBonusHp, 0);

        if (this.remainingHitPoints == 0) {
            this.die();
        }

        this.getParent().updateText();

        this.showMessage("-" + Integer.toString(dealtDamage));

        return this.remainingHitPoints == 0;
    }

    public void showMessage(String msg) {
        pawnMessage.setText(msg);
        messageShowTime = 0;
    }

    public void die() {
        System.out.println("DIE");
        getParent().removePawn();
        getScene().removePawn(this);
    }

    @Override
    public void onUpdate(float deltaTime) {
        messageShowTime += deltaTime;

        if (messageShowTime > 750 && pawnMessage.isVisible()) {
            pawnMessage.setVisibility(false);
        }
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
        this.unmoved = ready;
    }

    public void setUnmoved(boolean unmoved) {
        this.unmoved &= unmoved;
    }

    public void setReadyIfUnmoved() {
        ready = unmoved;
    }
}
