package com.raven.breakingsands;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDatable;

import java.util.HashMap;

public class Character implements GameDatable {

    private String name = "Cat", title = "Lord Commander";
    private int exp = 12, level = 2,
            hitPoints = 10, movement = 3, evasion = 7;
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
