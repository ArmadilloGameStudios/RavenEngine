package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.character.Armor;
import com.raven.breakingsands.character.Character;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.database.GameDatabase;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends WorldObject<BattleScene, Terrain, WorldObject> {
    private static GameDataList dataList = GameDatabase.all("pawn");

    public static GameDataList getDataList() {
        return dataList;
    }

    public static List<ModelData> getModelData() {
        List<ModelData> data = new ArrayList<>();

        for (GameData gameData : dataList) {
            data.add(GameEngine.getEngine().getModelData(gameData.getString("model")));
        }

        return data;
    }

    // instance
    private Weapon weapon;
    private Armor armor;
    private String name = "";
    private int team, hitPoints, totalMovement, remainingMovement, evasion, totalAttacks = 1, remainingAttacks;

    public Pawn(BattleScene scene, GameData gameData) {
        super(scene, gameData.getString("model"));

        name = gameData.getString("name");
        team = gameData.getInteger("team");
        hitPoints = gameData.getInteger("hp");
        totalMovement = gameData.getInteger("movement");
        evasion = gameData.getInteger("evasion");

        weapon = new Weapon(GameDatabase.all("weapon").getRandom());
        armor = new Armor(GameDatabase.all("armor").getRandom());
    }

    public Pawn(BattleScene scene, Character character) {
        super(scene, dataList.queryRandom(new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getString("name").equals("Player");
            }
        }).getString("model"));

        name = character.getTitle() + " " + character.getName();
        team = 0;
        hitPoints = character.getHitPoints();
        totalMovement = character.getMovement();
        evasion = character.getEvasion();

        weapon = new Weapon(GameDatabase.all("weapon").getRandom());
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

    public void attack(Pawn pawn) {
        remainingAttacks = Math.max(totalAttacks - 1, 0);

        // Check if hit
        int target = pawn.getWeapon().getAccuracy();
        int totalRange = pawn.evasion + target;
        int rolled = getScene().getRandom().nextInt(totalRange);

        System.out.println("Accuracy: " + target);
        System.out.println("Evasion: " + pawn.evasion);
        System.out.println("Total Range: " + totalRange);
        System.out.println("Rolled: " + rolled);

        if (rolled < target) {
            int remainingResistance = Math.max(pawn.armor.getResistance() - weapon.getPiercing(), 0);
            int dealtDamage = Math.max(weapon.getDamage() - remainingResistance, 0);
            pawn.hitPoints = Math.max(pawn.hitPoints - dealtDamage, 0);

            if (pawn.hitPoints == 0) {
                pawn.die();
            }

            pawn.getParent().updateText();
        } else {
            System.out.println("MISS");
        }
    }

    public void die() {
        getParent().removePawn();
        getScene().removePawn(this);

        getScene().checkVictory();
    }
}
