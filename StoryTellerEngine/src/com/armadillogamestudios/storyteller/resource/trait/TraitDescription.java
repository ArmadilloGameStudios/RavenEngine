package com.armadillogamestudios.storyteller.resource.trait;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class TraitDescription {

    private String name, defaultProperty = "name";
    private int maxValue, startingValue;

    private boolean removeAtZero = false;

    private GameDataList properties;

    public TraitDescription(GameData gameData) {
        name = gameData.getString("name");

        properties = gameData.getList("list");

        maxValue = properties.size() - 1;

        gameData.ifHas("default", gd -> defaultProperty = gd.asString());
        gameData.ifHas("remove_at_zero", gd -> removeAtZero = gd.asBoolean());
        gameData.ifHas("starting_value", i -> startingValue = i.asInteger());
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public String getProperty(int value, String name) {
        return properties.get(value).getString(name);
    }

    public String getProperty(int value) {
        return getProperty(value, defaultProperty);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isRemoveAtZero() {
        return removeAtZero;
    }
}
