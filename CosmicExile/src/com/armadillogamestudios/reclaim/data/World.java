package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.engine2d.util.pathfinding.PathFinder;
import com.armadillogamestudios.engine2d.util.pathfinding.PathInterpreter;
import com.armadillogamestudios.reclaim.ReclaimGame;
import com.armadillogamestudios.reclaim.util.BiomePathInterpreter;

import java.util.*;
import java.util.function.Consumer;

public class World implements GameDatable {

    private static final int worldEdgeLength = 80;
    private final Region[][] regionArray = new Region[worldEdgeLength][];
    private final List<Region> regions = new ArrayList<>();
    private int time = 0;
    private final List<Unit> units = new ArrayList<>();

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
        BiomeGroup biomeGroupForest = new BiomeGroup(GameDatabase.all("biomegroup").query("name", "forest"));
        BiomeGroup biomeGroupPlains = new BiomeGroup(GameDatabase.all("biomegroup").query("name", "plains"));
        BiomeGroup biomeGroupDesert = new BiomeGroup(GameDatabase.all("biomegroup").query("name", "desert"));
        BiomeGroup biomeGroupSteppe = new BiomeGroup(GameDatabase.all("biomegroup").query("name", "steppe"));
        BiomeGroup biomeGroupHighlands = new BiomeGroup(GameDatabase.all("biomegroup").query("name", "highlands"));

        List<BiomeGroup> biomeGroups = new ArrayList<>();

        biomeGroups.add(biomeGroupForest);
        biomeGroups.add(biomeGroupPlains);
        biomeGroups.add(biomeGroupDesert);
        biomeGroups.add(biomeGroupSteppe);
        biomeGroups.add(biomeGroupHighlands);

        HashMap<Region, BiomeGroup> regionBiomeGroupMap = new HashMap<>();

        int step = 8;

        for (int x = step; x < worldEdgeLength; x += step) {
            for (int y = step; y < worldEdgeLength; y += step) {

                int group = ReclaimGame.RANDOM.nextInt(biomeGroups.size());

                int xOffset = ReclaimGame.RANDOM.nextInt(7);
                int yOffset = ReclaimGame.RANDOM.nextInt(7);

                regionBiomeGroupMap.put(regionArray[x - 3 + xOffset][y - 3 + yOffset], biomeGroups.get(group));
            }
        }

        regionBiomeGroupMap.put(regionArray[worldEdgeLength / 2][worldEdgeLength / 2], biomeGroupPlains);

        PathFinder<Region> pathFinder = new PathFinder<>();
        BiomePathInterpreter interpreter = new BiomePathInterpreter();

        regions.forEach(r -> {
            Region closes = pathFinder
                    .findTarget(r, regionBiomeGroupMap.keySet(), interpreter)
                    .getLast()
                    .getNode();

            BiomeGroup biomeGroup = regionBiomeGroupMap.get(closes);

            r.setBiome(biomeGroup.get(r.getBiomeValue()));
        });

        // Set Spawn
        regionArray[worldEdgeLength / 2][worldEdgeLength / 2].setSettlement(
                new Settlement(GameDatabase.all("settlement").query("name", "fort"), true, ReclaimGame.getActiveGameData().getPlayers().get(0)));

        new Unit(ReclaimGame.getActiveGameData().getPlayers().get(0).getCapital(),
                this,
                GameDatabase.all("unit").query("name", "guard patrol"));
    }

    private void genRegion(int x, int y) {

        int biomeValue = ReclaimGame.RANDOM.nextInt(100);

        regionArray[x][y] = new Region(this, biomeValue, x, y);
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

    public int getDay() {
        return time / 24 + 1;
    }

    public int getHour() {
        return time % 24;
    }

    public int getTime() {
        return time;
    }

    public void tick() {
        time++;
    }

    void addUnit(Unit unit) {
        units.add(unit);
    }

    public List<Unit> getUnits() {
        return units;
    }
}
