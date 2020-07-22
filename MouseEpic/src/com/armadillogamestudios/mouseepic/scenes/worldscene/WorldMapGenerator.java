package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainListFactory;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class NeighborMap {
    // TODO add diagonals

    public enum Side {LEFT, RIGHT, BOTTOM, TOP,}

    private HashSet<GameData> neighborsLeft, neighborsRight, neighborsBottom, neighborsTop;

    public NeighborMap(GameData source, GameDataList choices) {
        TerrainListFactory terrainListFactory = new TerrainListFactory();

        // standard
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

    public HashSet<GameData> getSide(Side side) {
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
//            case LEFT_TOP:
//                return neighborsLeftTop;
//            case RIGHT_TOP:
//                return neighborsRightTop;
//            case LEFT_BOTTOM:
//                return neighborsLeftBottom;
//            case RIGHT_BOTTOM:
//                return neighborsRightBottom;
        }
    }
}

public class WorldMapGenerator {

    private WorldMapGenerator() {
    }

    private static Map<GameData, NeighborMap> terrainNeighborMap;
    private static int regionSize;
    private static Random random;

    public static GameData[] generateMap(int size, Random random) {
        WorldMapGenerator.regionSize = size;
        WorldMapGenerator.random = random;

        // create terrain neighbor sets
        terrainNeighborMap = new HashMap<>();
        GameDataTable terrainList = GameDatabase.all("terrain");
        terrainList.forEach(t -> terrainNeighborMap.put(t, new NeighborMap(t, terrainList)));

        // create subregions
        GameDataList[] patchedMap = new GameDataList[regionSize * regionSize];

        int s = 16;
        int c = size / s;

        for (int x = 0; x < c; x++) {
            for (int y = 0; y < c; y++) {
                GameDataList[] subregion;

                switch (random.nextInt(3)) {
                    default:
                    case 0:
                        subregion = generateSubRegion("plane");
                        break;
                    case 1:
                        subregion = generateSubRegion("plane");
                        break;
                    case 2:
                        subregion = generateSubRegion("plane");
                        break;
                }

                patchMap(patchedMap, subregion, x * s, y * s, s);
            }
        }

        GameData[] map = resolveMap(patchedMap);

        // clean up
        terrainNeighborMap = null;
        WorldMapGenerator.random = null;

        return map;
    }

    private static GameDataList[] generateSubRegion(String subregion) {
        // load data
        GameData subRegionGameData = GameDatabase.all(subregion).get(0);
        int size = subRegionGameData.getInteger("size");

        // create potentials map
        Map<String, GameDataList> keyPotentialsMap = new HashMap<>();
        GameDataList[] mapOfPotentials = new GameDataList[size * size];

        GameDataList srMapGameData = subRegionGameData.getList("map");

        // loop through map
        AtomicInteger indexY = new AtomicInteger(size - 1);
        srMapGameData.stream().map(GameData::asList).forEach(srRowGameData -> {

            AtomicInteger indexX = new AtomicInteger(0);
            srRowGameData.forEach(srGameData -> {
                String key = updateKeys(keyPotentialsMap, subRegionGameData, srGameData.asString());

                int i = pointToInt(indexX.get(), indexY.get(), size);

                mapOfPotentials[i] = new GameDataList(keyPotentialsMap.get(key));

                indexX.incrementAndGet();
            });

            indexY.decrementAndGet();
        });

        // add sections
        subRegionGameData.ifHas("sections", gds -> gds.asList().forEach(section -> {


            GameDataList sectionMap = section
                    .getList("maps")
                    .getRandom(random)
                    .asList();

            indexY.set(section.getInteger("y") + sectionMap.size() - 1);
            sectionMap.stream().map(GameData::asList).forEach(sRowGameData -> {
                AtomicInteger indexX = new AtomicInteger(section.getInteger("x"));

                sRowGameData.forEach(sGameData -> {
                    String key = updateKeys(keyPotentialsMap, subRegionGameData, sGameData.asString());

                    int i = pointToInt(indexX.get(), indexY.get(), size);

                    mapOfPotentials[i] = new GameDataList(keyPotentialsMap.get(key));

                    indexX.incrementAndGet();
                });

                indexY.decrementAndGet();
            });
        }));

        return mapOfPotentials;
    }

    private static String updateKeys(Map<String, GameDataList> keyPotentialsMap, GameData subRegionGameData, String key) {
        key = key.toLowerCase().replaceAll("\\s+", "");

        if (!keyPotentialsMap.containsKey(key)) {

            GameData rule;
            switch (key) {
                case "":
                    rule = subRegionGameData.getData("default");
                    break;
                default:
                    rule = subRegionGameData.getData(key);
                    break;
            }

            keyPotentialsMap.put(key, getPotentialsFromRule(rule));
        }

        return key;
    }

    private static void patchMap(GameDataList[] patchedMap, GameDataList[] subregion, int x, int y, int size) {
        for (int i = 0; i < size; i++) {
            System.arraycopy(subregion, i * size, patchedMap, (x + i) * regionSize + y, size);
        }
    }

    private static GameData[] resolveMap(GameDataList[] mapOfPotentials) {// load data

        GameData[] map = new GameData[regionSize * regionSize];

        // create potentials map
        Deque<GameDataList> potentialsToResolve = new ArrayDeque<>();
        HashMap<GameDataList, Integer> potentialIndexLookup = new HashMap<>();

        // process map
        HashSet<Integer> toUpdateCompare = new HashSet<>();
        Deque<Integer> toUpdate = new ArrayDeque<>();

        for (int i = 0; i < regionSize * regionSize; i++) {
            potentialIndexLookup.put(mapOfPotentials[i], i);
            toUpdate.add(i);
            toUpdateCompare.add(i);
        }

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

                int x = intToX(i, regionSize);
                int y = intToY(i, regionSize);

                GameDataList list = safeGatFromMap(mapOfPotentials, x, y, regionSize);

                if (list.size() > 1) {

                    GameDataList left = safeGatFromMap(mapOfPotentials, x - 1, y, regionSize);
                    if (left != null) {
                        updated |= reduceList(NeighborMap.Side.LEFT, list, left);
                    }

                    GameDataList right = safeGatFromMap(mapOfPotentials, x + 1, y, regionSize);
                    if (right != null) {
                        updated |= reduceList(NeighborMap.Side.RIGHT, list, right);
                    }

                    GameDataList bottom = safeGatFromMap(mapOfPotentials, x, y - 1, regionSize);
                    if (bottom != null) {
                        updated |= reduceList(NeighborMap.Side.BOTTOM, list, bottom);
                    }

                    GameDataList top = safeGatFromMap(mapOfPotentials, x, y + 1, regionSize);
                    if (top != null) {
                        updated |= reduceList(NeighborMap.Side.TOP, list, top);
                    }

                    GameDataList compare = safeGatFromMap(mapOfPotentials, x - 1, y + 1, regionSize);
                    if (compare != null && left != null && top != null) {
                        updated |= reduceList(
                                NeighborMap.Side.LEFT,
                                NeighborMap.Side.TOP,
                                list, left, top, compare);
                    }

                    compare = safeGatFromMap(mapOfPotentials, x - 1, y - 1, regionSize);
                    if (compare != null && left != null && bottom != null) {
                        updated |= reduceList(
                                NeighborMap.Side.LEFT,
                                NeighborMap.Side.BOTTOM,
                                list, left, bottom, compare);
                    }

                    compare = safeGatFromMap(mapOfPotentials, x + 1, y + 1, regionSize);
                    if (compare != null && top != null && right != null) {
                        updated |= reduceList(
                                NeighborMap.Side.RIGHT,
                                NeighborMap.Side.TOP,
                                list, right, top, compare);
                    }

                    compare = safeGatFromMap(mapOfPotentials, x + 1, y - 1, regionSize);
                    if (compare != null && bottom != null && right != null) {
                        updated |= reduceList(
                                NeighborMap.Side.RIGHT,
                                NeighborMap.Side.BOTTOM,
                                list, right, bottom, compare);
                    }

                    if (updated) {
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y - 1, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y + 1, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y - 1, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y - 1, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y + 1, regionSize);
                        addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y + 1, regionSize);
                    }
                }
            }

            noNulls = true;
            while (noNulls && !potentialsToResolve.isEmpty()) {
                GameDataList toResolve = potentialsToResolve.pop();

                if (toResolve.size() > 1) {
                    noNulls = false;

                    int i = potentialIndexLookup.get(toResolve);
                    int x = intToX(i, regionSize);
                    int y = intToY(i, regionSize);

                    GameData r = toResolve.getRandom(random);
                    toResolve.clear();
                    toResolve.add(r);

                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y - 1, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x, y + 1, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y - 1, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y - 1, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x - 1, y + 1, regionSize);
                    addIntToUpdate(toUpdate, toUpdateCompare, mapOfPotentials, x + 1, y + 1, regionSize);
                }
            }
        }

        for (int i = 0; i < regionSize * regionSize; i++) {
            GameDataList list = mapOfPotentials[i];

            if (list.size() == 1) {
                GameData r = list.getRandom(random);
                map[i] = r;
            }
        }

        return map;
    }

    private static <G> void addIntToUpdate(Deque<Integer> que, HashSet<Integer> compare, G[] map, int x, int y, int size) {
        int i = pointToInt(x, y, size);

        if (safeGatFromMap(map, x, y, size) != null && !compare.contains(i)) {
            que.add(i);
            compare.add(i);
        }
    }

    private static boolean reduceList(NeighborMap.Side side, GameDataList toReduce, GameDataList neighbor) {
        if (neighbor.size() == 0) return false;

        AtomicBoolean didReduce = new AtomicBoolean(false);

        List<NeighborMap> compare = new ArrayList<>();
        neighbor.forEach(n -> compare.add(terrainNeighborMap.get(n)));

        GameDataList copy = new GameDataList(toReduce);

        copy.forEach(terrain -> {
            if (compare.stream().noneMatch(set -> set.getSide(side).contains(terrain))) {
                toReduce.remove(terrain);
                didReduce.set(true);
            }
        });

        return didReduce.get();
    }

    private static boolean reduceList(
            NeighborMap.Side sideA, NeighborMap.Side sideB,
            GameDataList toReduce, GameDataList neighborA, GameDataList neighborB, GameDataList corner) {

        if (neighborA.isEmpty() || neighborB.isEmpty() || corner.isEmpty()) return false; // error

        AtomicBoolean didReduce = new AtomicBoolean(false);

        List<NeighborMap> cornerCompare = new ArrayList<>();
        corner.forEach(n -> cornerCompare.add(terrainNeighborMap.get(n)));

        GameDataList copy = new GameDataList(toReduce);

        copy.forEach(terrain -> {
            if (neighborA.stream().noneMatch(na -> {
                HashSet<GameData> compareA = terrainNeighborMap.get(na).getSide(sideA);

                if (compareA.contains(terrain)) {
                    return neighborB.stream().anyMatch(nb -> {
                        HashSet<GameData> compareB = terrainNeighborMap.get(nb).getSide(sideB);

                        if (compareB.contains(terrain)) {
                            return corner.stream().anyMatch(nc -> {
                                NeighborMap ncMap = terrainNeighborMap.get(nc);
                                HashSet<GameData> compareCornerB = ncMap.getSide(sideA);
                                HashSet<GameData> compareCornerA = ncMap.getSide(sideB);

                                return compareCornerA.contains(na) && compareCornerB.contains(nb);
                            });
                        } else return false;
                    });
                } else return false;
            })) {
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
        rule.ifHas("types", l -> list.removeIf(d -> d.getList("type").stream().noneMatch(t -> l.asList().stream().anyMatch(r -> r.asString().equals(t.asString())))));

        return list;
    }

    private static <T> T safeGatFromMap(T[] map, int x, int y, int size) {
        if (x >= 0 && x < size && y >= 0 && y < size)
            return map[pointToInt(x, y, size)];
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

    private static int pointToInt(int x, int y, int size) {
        return x * size + y;
    }

    private static int intToX(int i, int size) {
        return i / size;
    }

    private static int intToY(int i, int size) {
        return i % size;
    }

}
