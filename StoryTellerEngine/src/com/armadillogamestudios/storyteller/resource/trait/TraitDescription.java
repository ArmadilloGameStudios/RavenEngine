package com.armadillogamestudios.storyteller.resource.trait;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;

import java.util.List;
import java.util.stream.Collectors;

public class TraitDescription {

    private String key, name, defaultProperty = "name", type;
    private int maxValue, startingValue;

    private boolean removeAtZero = false;

    private GameDataList properties;

    public TraitDescription(GameData gameData) {
        key = gameData.getString("key");
        gameData.ifHas("name", (gd) -> name = gd.asString(), () -> name = key);

        properties = gameData.getList("list");

        maxValue = properties.size() - 1;

        gameData.ifHas("type", gd -> type = gd.asString());
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

    public List<String> getProperties(String name) {
        return properties.stream().map((prop) -> prop.getString(name)).collect(Collectors.toList());
    }

    public List<String> getProperties() {
        return getProperties(defaultProperty);
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return key;
    }

    public boolean isRemoveAtZero() {
        return removeAtZero;
    }
}
