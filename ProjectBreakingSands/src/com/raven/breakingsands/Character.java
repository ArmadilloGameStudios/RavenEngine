package com.raven.breakingsands;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDatable;

import java.util.HashMap;

public class Character implements GameDatable {

    private String name = "Cat", title = "Lord Commander";
    private int exp = 12;
    private int level = 2;
    private int hitPoints = 10;
    private int movement = 3;
    private int evasion = 7;
    private Weapon weapon;
    private Armor armor;

    public Character() {

    }

    public Character(GameData gameData) {
        name = gameData.getString("name");
        title = gameData.getString("title");
        exp = gameData.getInteger("exp");
        level = gameData.getInteger("level");
        movement = gameData.getInteger("movement");
        evasion = gameData.getInteger("evasion");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public void setEvasion(int evasion) {
        this.evasion = evasion;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getExp() {
        return exp;
    }

    public void addExperience(int exp) {
        this.exp += exp;
    }

    public int getLevel() {
        return level;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMovement() {
        return movement;
    }

    public int getEvasion() {
        return evasion;
    }

    public boolean canLevelUp() {
        return exp > 5;
    }

    @Override
    public GameData toGameData() {
        HashMap<String, GameData> data = new HashMap<>();

        data.put("name", new GameData(name));
        data.put("title", new GameData(title));
        data.put("exp", new GameData(exp));
        data.put("level", new GameData(level));
        data.put("hitPoints", new GameData(hitPoints));
        data.put("movement", new GameData(movement));
        data.put("evasion", new GameData(evasion));
//        data.put("weapon", weapon.toGameData());
//        data.put("armor", armor.toGameData());

        return new GameData(data);
    }
}
