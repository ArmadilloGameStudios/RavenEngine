package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.reclaim.CosmicExileGame;
import com.armadillogamestudios.tactics.gameengine.scene.map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class World implements GameDatable {

    private static final int worldEdgeLength = 76;

    private final Region[][] regionArray = new Region[worldEdgeLength][];
    private final List<Region> regions = new ArrayList<>();

    public World() {
        gen();
    }

    private void gen() {
        // Create Map

        // Create Regions
        for (int x = 0; x < worldEdgeLength; x++) {
            regionArray[x] = new Region[worldEdgeLength];

            for (int y = 0; y < worldEdgeLength; y++) {
                genRegion(x, y);
            }
        }

        // Biomes
        regions.forEach(r -> r.setBiome(Region.Biome.Planes));

        // Set Spawn
        regionArray[worldEdgeLength / 2][worldEdgeLength / 2].setBuilding(
                new Building(Building.Type.Fort, CosmicExileGame.getActiveGameData().getPlayers().get(0)));
    }

    private void genRegion(int x, int y) {
        regionArray[x][y] = new Region(x, y);
        regions.add(regionArray[x][y]);
        setAdjacent(x, y);
    }

    private void setAdjacent(int x, int y) {
        if (x > 0) {
            if (y % 2 != 0) {
                regionArray[x][y].setBottomLeftAdjacent(regionArray[x - 1][y - 1]);
                if (y < worldEdgeLength - 1)
                    regionArray[x][y].setTopLeftAdjacent(regionArray[x - 1][y + 1]);
            }

            regionArray[x][y].setLeftAdjacent(regionArray[x - 1][y]);
        }

        if (y > 0) {
            if (y % 2 == 0) {
                regionArray[x][y].setBottomLeftAdjacent(regionArray[x][y - 1]);
            } else {
                regionArray[x][y].setBottomRightAdjacent(regionArray[x][y - 1]);
            }
        }
    }

    @Override
    public GameData toGameData() {
        return null;
    }

    public void forEachRegion(Consumer<Region> action) {
        regions.forEach(action);
    }

    public Region[][] getRegions() {
        return regionArray;
    }
}
