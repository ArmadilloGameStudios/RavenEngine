package com.raven.breakingsands.scenes.battlescene.pawn;

import com.raven.breakingsands.Armor;
import com.raven.breakingsands.Weapon;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.terrain.Terrain;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
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
    private GameData gameData;
    private Weapon weapon;
    private Armor armor;
    private String name = "";
    private int team, hitPoints, movement, evasion;

    public Pawn(BattleScene scene, GameData gameData) {
        super(scene, gameData.getString("model"));

        this.gameData = gameData;

        name = gameData.getString("name");
        team = gameData.getInteger("team");
        hitPoints = gameData.getInteger("hp");
        movement = gameData.getInteger("movement");
        evasion = gameData.getInteger("evasion");

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

    public int getMovement() {
        return movement;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Armor getArmor() {
        return armor;
    }

    public void attack(Pawn pawn) {
        // Check if hit
        int target = pawn.getWeapon().getAccuracy();
        int totalRange = this.evasion + target;
        int rolled = getScene().getRandom().nextInt(totalRange);

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
    }
}
