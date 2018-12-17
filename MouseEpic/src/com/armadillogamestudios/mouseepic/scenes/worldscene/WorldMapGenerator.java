package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainListFactory;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.database.GameDatabase;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class WorldMapGeneratorNeighborMap {
    // TODO add diagonals

    public enum Side {LEFT, RIGHT, BOTTOM, TOP}

    private HashSet<GameData> neighborsLeft, neighborsRight, neighborsBottom, neighborsTop;

    public WorldMapGeneratorNeighborMap(GameData source, GameDataList choices) {
        TerrainListFactory terrainListFactory = new TerrainListFactory();

        terrainListFactory.clear();
        terrainListFactory.setChoiceList(choices);
        terrainListFactory.setLeft(source);
        neighborsLeft = new HashSet<>(terrainListFactory.getInstance());

        terrainListFactory.clear();
        terrainListFactory.setChoiceList(choices);
        terrainListFactory.setRight(source);
        neighborsRight = new HashSet<>(terrainListFactory.getInstance());

        terrainListFactory.clear();
        terrainListFactory.setChoiceList(choices);
        terrainListFactory.setBottom(source);
        neighborsBottom = new HashSet<>(terrainListFactory.getInstance());

        terrainListFactory.clear();
        terrainListFactory.setChoiceList(choices);
        terrainListFactory.setTop(source);
        neighborsTop = new HashSet<>(terrainListFactory.getInstance());
    }

    public HashSet<GameData> getSet(Side side) {
        switch (side) {
            default:
            case LEFT:
                return neighborsLeft;
            case RIGHT:
                return neighborsRight;
            case BOTTOM:
                return neighborsBottom;
            case TOP:
                return neighborsTop;
        }
    }
}

public class WorldMapGenerator {

    private WorldMapGenerator() {
    }

    private static Map<GameData, WorldMapGeneratorNeighborMap> terrainNeighborMap;
    private static GameData[] map;
    private static int size;
    private static Random random;

    public static GameData[] generateMap(int size, Random random) {
        WorldMapGenerator.size = size;
        WorldMapGenerator.random = random;

        map = new GameData[size * size];

        generateSubRegion("pond");

        // clean up
        GameData[] returnMap = map;
        map = null;
        terrainNeighborMap = null;
        WorldMapGenerator.random = null;

        return returnMap;
    }

    private static void generateSubRegion(String subregion) {
        // load data
        GameData subregionGameData = GameDatabase.all(subregion).get(0);

        // create potentials map
        Map<String, GameDataList> keyPotentialsMap = new HashMap<>();
        GameDataList[] mapOfPotentials = new GameDataList[size * size];
        Deque<GameDataList> potentialsToResolve = new ArrayDeque<>();
        HashMap<GameDataList, Integer> potentialIndexLookup = new HashMap<>();

        // create terrain neighbor sets
        terrainNeighborMap = new HashMap<>();

        GameDataTable terrainList = GameDatabase.all("terrain");
        terrainList.forEach(t -> terrainNeighborMap.put(t, new WorldMapGeneratorNeighborMap(t, terrainList)));

        // process map
        HashSet<Integer> toUpdateCompare = new HashSet<>();
        Deque<Integer> toUpdate = new ArrayDeque<>();

        GameDataList srMapGameData = subregionGameData.getList("map");

        AtomicInteger indexY = new AtomicInteger(size - 1);
        srMapGameData.stream().map(GameData::asList).forEach(srRowGameData -> {

            AtomicInteger indexX = new AtomicInteger(0);
            srRowGameData.forEach(srGameData -> {

                String key = srGameData.asString().toLowerCase().replaceAll("\\s+", "");

                if (!keyPotentialsMap.containsKey(key)) {

                    GameData rule;
                    switch (key) {
                        case "":
                            rule = subregionGameData.getData("default");
                            break;
                        default:
                            rule = subregionGameData.getData(key);
                            break;
                    }

                    keyPotentialsMap.put(key, getPotentialsFromRule(rule));
                }

                int i = pointToInt(indexX.get(), indexY.get());

                mapOfPotentials[i] = new GameDataList(keyPotentialsMap.get(key));
                potentialIndexLookup.put(mapOfPotentials[i], i);
                toUpdate.add(i);
                toUpdateCompare.add(i);

                indexX.incrementAndGet();
            });

            indexY.decrementAndGet();
        });

        // randomize resolve queue
        List<GameDataList> toPick = new ArrayList<>(Arrays.asList(mapOfPotentials));
        while (!toPick.isEmpty()) {
            potentialsToResolve.add(toPick.remove(random.nextInt(toPick.size())));
        }

        // update all
        boolean noNulls = false;
        while (!noNulls) {
            while (!toUpdate.isEmpty()) {
                boolean updated = false;

                int i = toUpdate.pop();
                toUpdateCompare.remove(i);

                int x = intToX(i);
                int y = intToY(i);

                GameDataList list = safeGatFromMap(mapOfPotentials, x, y, size);

                if (list.size() > 1) {

                    GameDataList left = safeGatFromMap(mapOfPotentials, x - 1, y, size);
                    if (left != null) {
                        updated |= reduceList(WorldMapGeneratorNeighborMap.Side.LEFT, list, left);
                    }

                    GameDataList right = safeGatFromMap(mapOfPotentials, x + 1, y, size);
                    if (right != null) {
                        updated |= reduceList(WorldMapGeneratorNeighborMap.Side.RIGHT, list, right);
                    }

                    GameDataList bottom = safeGatFromMap(mapOfPotentials, x, y - 1, size);
                    if (bottom != null) {
                        updated |= reduceList(WorldMapGeneratorNeighborMap.Side.BOTTOM, list, bottom);
                    }

                    GameDataList top = safeGatFromMap(mapOfPotentials, x, y + 1, size);
                    if (top != null) {
                        updated |= reduceList(WorldMapGeneratorNeighborMap.Side.TOP, list, top);
                    }

                    if (updated) {
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y - 1);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y + 1);
                    }
                }
            }

            noNulls = true;
            while (noNulls && !potentialsToResolve.isEmpty()) {
                GameDataList toResolve = potentialsToResolve.pop();

                if (toResolve.size() > 1) {
                    noNulls = false;

                    int i = potentialIndexLookup.get(toResolve);
                    int x = intToX(i);
                    int y = intToY(i);

                    GameData r = toResolve.getRandom(random);
                    toResolve.clear();
                    toResolve.add(r);

                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y - 1);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y + 1);
                }
            }
        }

        for (int i = 0; i < size * size; i++) {
            GameDataList list = mapOfPotentials[i];

            if (list.size() == 1) {
                GameData r = list.getRandom(random);
                map[i] = r;
            }
        }
    }

    private static <G> void addIntToUpdate(Deque<Integer> que, HashSet<Integer> compare, G[] map, int x, int y) {
        int i = pointToInt(x, y);

        if (safeGatFromMap(map, x, y, size) != null && !compare.contains(i)) {
            que.add(i);
            compare.add(i);
        }
    }

    private static boolean reduceList(WorldMapGeneratorNeighborMap.Side side, GameDataList toReduce, GameDataList neighbor) {
        if (neighbor.size() == 0) return false;

        AtomicBoolean didReduce = new AtomicBoolean(false);

        List<WorldMapGeneratorNeighborMap> compare = new ArrayList<>();
        neighbor.forEach(n -> compare.add(terrainNeighborMap.get(n)));

        GameDataList copy = new GameDataList(toReduce);

        copy.forEach(terrain -> {
            if (compare.stream().noneMatch(set -> set.getSet(side).contains(terrain))) {
                toReduce.remove(terrain);
                didReduce.set(true);
            }
        });

        return didReduce.get();
    }

    private static GameDataList getPotentialsFromRule(GameData rule) {
        GameDataList list = new GameDataList(GameDatabase.all("terrain"));
        rule.ifHas("strict", r -> list.removeIf(d -> !d.getString("name").equals(r.asString())));
        rule.ifHas("type", r -> list.removeIf(d -> d.getList("type").stream().noneMatch(t -> t.asString().equals(r.asString()))));

        return list;
    }

    private static <T> T safeGatFromMap(T[] map, int x, int y, int size) {
        if (x >= 0 && x < size && y >= 0 && y < size)
            return map[pointToInt(x, y)];
        else
            return null;
    }

    private static <T> boolean safeSetToMap(T[] map, T value, int x, int y, int size) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            map[x * size + y] = value;
            return true;
        } else
            return false;
    }

    private static int pointToInt(int x, int y) {
        return x * size + y;
    }

    private static int intToX(int i) {
        return i / size;
    }

    private static int intToY(int i) {
        return i % size;
    }

}
