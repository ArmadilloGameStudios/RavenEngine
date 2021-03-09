package com.armadillogamestudios.cosmicexile.data.contract;

import com.armadillogamestudios.cosmicexile.data.location.Location;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

public class Contract implements GameDatable {

    private final String name;
    private final String description;
    private final Location location;

    public Contract(String name, String description) {
        this.name = name;
        this.description = description;

        this.location = new Location(name);
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
