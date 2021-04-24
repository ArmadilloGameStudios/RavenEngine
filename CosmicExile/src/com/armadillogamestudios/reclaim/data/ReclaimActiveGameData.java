package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.ArrayList;
import java.util.List;

public class ReclaimActiveGameData implements GameDatable {

    private final List<Player> players = new ArrayList<>();
    private World world;

    @Override
    public GameData toGameData() {
        return null;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
