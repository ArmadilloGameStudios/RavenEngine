package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.ArrayList;
import java.util.List;

public class Player implements GameDatable {

    private int gold = 1000;
    private Settlement settlement;
    private List<Unit> units = new ArrayList<>();

    @Override
    public GameData toGameData() {
        return null;
    }

    public void addGold(int amount) {
        gold += amount;
    }

    public int getGold() {
        return gold;
    }

    public void setCapital(Settlement settlement) {
        this.settlement = settlement;
    }

    public Settlement getCapital() {
        return settlement;
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }
}
