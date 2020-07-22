package com.armadillogamestudios.storyteller.manager;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;

import java.util.HashMap;
import java.util.Map;

public class Manager<T> {

    private Map<String, T> map = new HashMap<>();

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public T get(String key) {
        return map.get(key);
    }

    public void put(String name, T value) {
        map.put(name, value);
    }
}
