package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.ZLayer;
import com.raven.breakingsands.character.Ability;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.character.WeaponType;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.graphics2d.sprite.handler.ActionFinishHandler;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Pawn extends WorldObject<BattleScene, Terrain, WorldObject>
        implements GameDatable {

    public static GameDataList getDataList() {
        return GameDatabase.all("pawn");
    }

    public static List<SpriteSheet> getSpriteSheets(BattleScene scene) {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : getDataList()) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    // instance
    private Weapon weapon;
    private String name = "", charClass = "amateur", spriteHack, weaponHack;
    private int level = 0, xp, team,
            hitPoints, remainingHitPoints, bonusHp, bonusHpLoss,
            totalShield, remainingShield, bonusShield, bonusShieldLoss,
            totalMovement, remainingMovement,
            resistance, bonusResistance, totalAttacks = 1, remainingAttacks,
            xpGain;
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
        gameData.ifHas("class", c -> charClass = c.asString());
        gameData.ifHas("level", l -> level = l.asInteger());
        gameData.ifHas("xp", x -> xp = x.asInteger());
        team = gameData.getInteger("team");

        gameData.ifHas("sprite_hack", h -> spriteHack = h.asString());
        gameData.ifHas("weapon_hack", h -> weaponHack = h.asString());

        gameData.ifHas("bonus_hp_loss", g -> bonusHpLoss = g.asInteger()); // TODO check after abilities
        gameData.ifHas("remaining_hit_points",
                r -> {
                    remainingHitPoints = r.asInteger();
                    hitPoints = gameData.getInteger("hp");
                },
                () -> remainingHitPoints = hitPoints = gameData.getInteger("hp"));


        gameData.ifHas("bonus_shield_loss", g -> bonusShieldLoss = g.asInteger()); // TODO check after abilities
        gameData.ifHas("remaining_shield",
                r -> {
                    remainingShield = r.asInteger();
                    totalShield = gameData.getInteger("shield");
                },
                () -> gameData.ifHas("shield", s -> remainingShield = totalShield = s.asInteger()));

        totalMovement = gameData.getInteger("movement");

        gameData.ifHas("remaining_movement", m -> remainingMovement = m.asInteger());
        gameData.ifHas("resistance", m -> resistance = m.asInteger());
        gameData.ifHas("total_attacks", m -> totalAttacks = m.asInteger());
        gameData.ifHas("remaining_attacks", m -> remainingAttacks = m.asInteger());
        gameData.ifHas("xp_gain", x -> xpGain = x.asInteger());

        GameDatabase db = scene.getEngine().getGameDatabase();

        // weapon
        if (gameData.has("weapon")) {
            GameData gdWeapon = gameData.getData("weapon");

            if (gdWeapon.isString()) {
                db.getTable("weapon").stream()
                        .filter(w -> w.getString("name").equals(gdWeapon.asString()))
                        .findFirst()
                        .ifPresent(w -> weapon = new Weapon(scene, w));
            } else {
                weapon = new Weapon(scene, gdWeapon);
            }
        } else {
            weapon = new Weapon(scene, db.getTable("weapon").getRandom(scene.getRandom()));
        }

        if (weapon != null) {
            addChild(weapon);
        }

        // abilities
        if (gameData.has("abilities")) {
            for (GameData gdAbility : gameData.getList("abilities")) {
                addAbility(new Ability(gdAbility), false);
            }
        }

        // hack
        gameData.ifHas("hack", h -> hack(new Hack(scene, h)));

        gameData.ifHas("unmoved", x -> unmoved = x.asBoolean());
        gameData.ifHas("ready", x -> ready = x.asBoolean());

        // message
        pawnMessage = new PawnMessage(scene);
        Vector2f pos = pawnMessage.getWorldPosition();
        pos.x -= .9;
        pos.y += 1.3;
        pawnMessage.setPosition(pos);
        addChild(pawnMessage);
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> map = new HashMap<>();

        GameData woData = getWorldObjectData();
        for (String key : woData.asMap().keySet()) {
            map.put(key, woData.getData(key));
        }

        map.put("name", new GameData(name));
        map.put("class", new GameData(charClass));
        if (spriteHack != null)
        map.put("sprite_hack", new GameData(spriteHack));
        map.put("weapon_hack", new GameData(weaponHack));
        map.put("level", new GameData(level));
        map.put("xp", new GameData(xp));
        map.put("team", new GameData(team));
        map.put("hp", new GameData(hitPoints));
        map.put("remaining_hit_points", new GameData(remainingHitPoints));
        map.put("bonus_hp_loss", new GameData(bonusHpLoss));
        map.put("shield", new GameData(totalShield));
        map.put("remaining_shield", new GameData(remainingShield));
        map.put("bonus_shield_loss", new GameData(bonusShieldLoss));
        map.put("movement", new GameData(totalMovement));
        map.put("remaining_movement", new GameData(remainingMovement));
        map.put("resistance", new GameData(resistance));
        map.put("total_attacks", new GameData(totalAttacks));
        map.put("remaining_attacks", new GameData(remainingAttacks));
        map.put("xp_gain", new GameData(xpGain));
        map.put("weapon", weapon.toGameData());
        map.put("abilities", new GameDataList(abilities).toGameData());
        if (hack != null) {
            map.put("hack", hack.toGameData());
        }

        map.put("ready", new GameData(ready));
        map.put("unmoved", new GameData(unmoved));

        return new GameData(map);
    }

    public String getName() {
        return name;
    }

    public int getTeam(boolean withHack) {
        if (withHack) {
            if (hack == null)
                return team;
            else
                return hack.getTeam();
        } else
            return team;
    }

    public void setTeam(int i) {
        team = i;
    }

    public void hack(Hack hack) {
        this.hack = hack;
        if (hack.isInstant()) {
            ready();
        } else {
            setReady(false);
        }
        if (this.spriteHack != null)
            this.setSpriteSheet(spriteHack);
        if (this.weaponHack != null) {

            removeChild(weapon);

            GameDatabase.all("weapon").stream()
                    .filter(w -> w.getString("name").equals(weaponHack))
                    .findFirst()
                    .ifPresent(w -> weapon = new Weapon(getScene(), w));

            addChild(weapon);
        }
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
        return Math.max(bonusHp + bonusHpLoss, 0);
    }

    public int getTotalShield() {
        return totalShield;
    }

    public int getRemainingShield() {
        return remainingShield;
    }

    public int getBonusShield() {
        return Math.max(bonusShield + bonusShieldLoss, 0);
    }

    public boolean canMove() {
        boolean canMove = true;

        canMove &= remainingMovement > 0;

//        canMove &= remainingAttacks == totalAttacks;
//
//        for (Ability a : abilities) {
//            canMove &= a.uses == null || (a.remainingUses == a.uses || a.remain);
//        }

        return canMove;
    }

    public boolean canLevel() {
        return xp >= getNextLevelXp();
    }

    public boolean canAttack() {
        final boolean[] canMove = {true};

        canMove[0] &= remainingAttacks == totalAttacks;

        abilities.stream().filter(a -> a.upgrade == null).forEach(a -> {
            if (a.useRegainType == Ability.UseRegainType.TURN)
                canMove[0] &= a.uses == null || (!a.usedThisTurn || a.remainingUses.equals(a.uses) || a.remain);
            else
                canMove[0] &= a.uses == null || (!a.usedThisTurn || a.remain);
        });

        return canMove[0];
    }

    public boolean canAbility(Ability ability) {
        final boolean[] canMove = {true};

        canMove[0] &= remainingAttacks == totalAttacks || abilities.stream().anyMatch(a -> a.remain);

        abilities.stream().filter(a -> a.upgrade == null).forEach(a -> {
            if (a == ability) {
                canMove[0] &= a.uses == null || (a.remainingUses > 0);
            } else if (a.useRegainType == Ability.UseRegainType.TURN)
                canMove[0] &= a.uses == null || (!a.usedThisTurn || a.remainingUses.equals(a.uses) || a.remain);
            else
                canMove[0] &= a.uses == null || (!a.usedThisTurn || a.remain);
        });

        return canMove[0];
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
        return resistance + bonusResistance;
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
        addAbility(ability, true);
    }

    public void addAbility(Ability ability, boolean add) {
        ability.owner = this;

        if (ability.replace != null) {
            abilities.stream()
                    .filter(a -> a.name.equals(ability.replace))
                    .findFirst()
                    .ifPresent(this::removeAbility);
        }
        abilities.add(ability);

        if (ability.type == Ability.Type.SELF) {
            if (ability.upgrade == null && add) {
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
//                                ability.size = null;
                            }
                            if (ability.damage != null) {
                                if (a.damage == null)
                                    a.damage = ability.damage;
                                else
                                    a.damage += ability.damage;
//                                ability.damage = null;
                            }
                            if (ability.turns != null) {
                                if (a.turns == null)
                                    a.turns = ability.turns;
                                else
                                    a.turns += ability.turns;
//                                ability.turns = null;
                            }
                            if (ability.uses != null) {
                                if (a.uses == null) {
                                    a.uses = ability.uses;
                                    a.remainingUses = ability.uses;
                                } else {
                                    a.uses += ability.uses;
                                    a.remainingUses += ability.uses;
                                }
                                ability.remainingUses = 0;
                            }
                            if (ability.instant_hack) {
                                a.instant_hack = true;
                            }
                            a.remain |= ability.remain;
                        });
            }
        }

        if (getParent() != null)
            getParent().setPawn(this); // Shitty way of making sure the aurora effect is there
    }

    public void removeAbility(Ability ability) {
        abilities.remove(ability);
        getParent().removePawnAbility(ability);

        if (ability.upgrade != null)
            abilities.stream()
                    .filter(a -> a.name.equals(ability.upgrade))
                    .forEach(a -> {
                        if (ability.size != null) {
                            a.size -= ability.size;
                        }
                        if (ability.damage != null) {
                            a.damage -= ability.damage;
                        }
                        if (ability.turns != null) {
                            a.turns -= ability.turns;
                        }
                        if (ability.uses != null) { // TODO
                            a.uses -= ability.uses;
                            a.remainingUses -= ability.uses;
                        }
                        if (ability.instant_hack) {
                            a.instant_hack = false;
                        }
                        a.remain = false;
                    });


    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void addAbilityAffect(Ability a) {
        if (a.target == Ability.Target.ALL ||
                (a.target == Ability.Target.ALLY && getTeam(true) == 0) ||
                (a.target == Ability.Target.ENEMY && getTeam(false) == 1)) {

            abilityAffects.add(a);

            if (a.hp != null)
                this.bonusHp += a.hp;
            if (a.shield != null)
                this.bonusShield += a.shield;
            if (a.resistance != null)
                this.bonusResistance += a.resistance;

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
                this.bonusResistance -= a.resistance;

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
            if (a.uses != null && a.useRegainType == Ability.UseRegainType.TURN) {
                a.remainingUses = a.uses;
            }
            a.usedThisTurn = false;
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

        if (weapon.getWeaponType() == WeaponType.MELEE) {
            runMeleeAnimation(target, directional, directionUp, onAttackDone);
        } else if (weapon.getWeaponType() == WeaponType.RANGED) {
            runRangedAnimation(target, directional, directionUp, onAttackDone);
        }
    }

    private void runMeleeAnimation(Pawn target, boolean directional, boolean directionUp, ActionFinishHandler onAttackDone) {

        if (directional)
            if (directionUp)
                getAnimationState().setAction("melee up start");
            else
                getAnimationState().setAction("melee down start");
        else
            getAnimationState().setAction("melee start");

        getAnimationState().addActionFinishHandler(x -> {

            if (directional)
                if (directionUp)
                    getAnimationState().setAction("melee up end");
                else
                    getAnimationState().setAction("melee down end");
            else
                getAnimationState().setAction("melee end");

            Effect effect = weapon.getEffect();
            if (effect != null) {
                effect.setVisibility(true);
                target.addChild(effect);
                effect.getAnimationState().addActionFinishHandler(an -> target.removeChild(effect));
            }

            AtomicReference<Boolean> a = new AtomicReference<>(false);
            AtomicReference<Boolean> b = new AtomicReference<>(false);

            Pawn p = this;
            ActionFinishHandler handlerA = animationState -> {
                a.set(true);

                if (b.get()) {
                    onAttackDone.onActionFinish(animationState);
                }
            };

            ActionFinishHandler handlerB = animationState -> {
                b.set(true);

                if (a.get()) {
                    onAttackDone.onActionFinish(animationState);
                }
            };

            attack(target, weapon.getDamage(), weapon.getPiercing(), weapon.getShots(), handlerB);
            getAnimationState().addActionFinishHandler(handlerA);
            getAnimationState().addActionFinishHandler(cat -> cat.setActionIdle(false));

        });

        weapon.runAttackAnimation(directionUp);
    }

    private void runRangedAnimation(Pawn target, boolean directional, boolean directionUp, ActionFinishHandler onAttackDone) {

        AtomicInteger shotCount = new AtomicInteger(weapon.getShots());

        if (directional)
            if (directionUp)
                getAnimationState().setAction("ranged up start");
            else
                getAnimationState().setAction("ranged down start");
        else
            getAnimationState().setAction("ranged start");

        getAnimationState().addActionFinishHandler(new PawnShotsActionFinishHandler(this, target, shotCount, directional, directionUp, onAttackDone));

        weapon.runAttackAnimation(directionUp);
    }

    public int getDamage(int damage, int percing, int shots) {
        int remainingResistance = Math.max(getResistance() - percing, 0);
        return Math.max(Math.max(damage - remainingResistance, 0) * shots, 1);
    }

    public void attack(Pawn pawn, int damage, int percing, int shots, ActionFinishHandler onAttackDone) {
        reduceAttacks();

        int dealtDamage = pawn.getDamage(damage, percing, shots);

        setUnmoved(false);

        if (hack != null) {
            if (pawn.damage(dealtDamage, onAttackDone) && getTeam(true) == 0) {
                hack.getHacker().xp += pawn.xpGain;
                hack.getHacker().showMessage("+" + Integer.toString(pawn.xpGain) + "xp");

                getScene().getPawns().stream()
                        .filter(p -> p.getTeam(false) == 0 && p != hack.getHacker())
                        .forEach(p -> {
                            p.xp += pawn.xpGain / 3;
                            p.showMessage("+" + Integer.toString(pawn.xpGain / 3) + "xp");
                        });
            }
        } else {
            if (pawn.damage(dealtDamage, onAttackDone) && getTeam(false) == 0) {
                xp += pawn.xpGain;
                showMessage("+" + Integer.toString(pawn.xpGain) + "xp");

                getScene().getPawns().stream()
                        .filter(p -> p.getTeam(false) == 0 && p != this)
                        .forEach(p -> {
                            p.xp += pawn.xpGain / 3;
                            p.showMessage("+" + Integer.toString(pawn.xpGain / 3) + "xp");
                        });
            }
        }
    }

    public void reduceAttacks() {
        remainingAttacks = Math.max(totalAttacks - 1, 0);

        if (remainingAttacks == 0) {
            setUnmoved(false);
        }
    }

    public boolean damage(int dealtDamage, ActionFinishHandler onAttackDone) {
        if (dealtDamage <= 0) {
            dealtDamage = 1;
        }

        int rolloverBonusShieldDamage = dealtDamage;
        if (getBonusShield() > 0) {
            rolloverBonusShieldDamage = -Math.min(getBonusShield() - dealtDamage, 0);
            this.bonusShieldLoss = Math.min(getBonusShield() - dealtDamage, -this.bonusShield);
        }

        int rolloverShieldDamage = -Math.min(this.remainingShield - rolloverBonusShieldDamage, 0);
        this.remainingShield = Math.max(this.remainingShield - rolloverBonusShieldDamage, 0);

        int rolloverBonusHp = rolloverShieldDamage;
        if (getBonusHp() > 0) {
            rolloverBonusHp = -Math.min(getBonusHp() - rolloverShieldDamage, 0);
            this.bonusHpLoss = Math.min(getBonusHp() - rolloverShieldDamage, 0);
        }
        this.remainingHitPoints = Math.max(this.remainingHitPoints - rolloverBonusHp, -this.bonusHp);

        if (this.remainingHitPoints <= 0) {
            this.die(onAttackDone);
        } else if (onAttackDone != null) {
            onAttackDone.onActionFinish(getAnimationState());
        }

        this.getParent().updateText();

        this.showMessage("-" + Integer.toString(dealtDamage));

        return this.remainingHitPoints <= 0;
    }

    public void showMessage(String msg) {
        pawnMessage.setText(msg);
        messageShowTime = 0;
    }

    public void die(ActionFinishHandler onAttackDone) {
        if (getAnimationState().hasAction("die")) {
            getAnimationState().setAction("die");
            getAnimationState().addActionFinishHandler(a -> onDie());
            if (onAttackDone != null)
                getAnimationState().addActionFinishHandler(onAttackDone);
        } else {
            onDie();
            if (onAttackDone != null)
                onAttackDone.onActionFinish(getAnimationState());
        }
    }

    private void onDie() {
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
    public Layer.Destination getDestination() {
        return Layer.Destination.Details;
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

    public void prepLevel() {
        bonusHpLoss = 0;
        bonusShieldLoss = 0;
        bonusResistance = 0;
        remainingShield = totalShield;

        abilities.forEach(a -> {
            if (a.uses != null) {
                a.remainingUses = a.uses;
                a.usedThisTurn = false;
            }
        });

        ready();
    }

    public int getXp() {
        return xp;
    }

    public int getNextLevelXp() {
        return (level * (level + 1) + 1) * 50;
    }
}
