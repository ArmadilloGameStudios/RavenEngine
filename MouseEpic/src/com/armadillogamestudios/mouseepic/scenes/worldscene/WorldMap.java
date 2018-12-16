package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainFactory;
import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.TerrainListFactory;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class WorldMap extends WorldObject<WorldScene, Terrain, WorldObject> {

    private TerrainFactory terrainFactory;
    private TerrainListFactory terrainListFactory = new TerrainListFactory();
    private Terrain[][] map;

    private int size;
    private int tries = 0;

    public WorldMap(WorldScene scene, int size) {
        super(scene);

        this.size = size;

        terrainFactory = new TerrainFactory(scene, GameDatabase.all("terrain"));
        map = new Terrain[size][];

        generateSubRegion("pond");
    }

    private void generateSubRegion(String subregion) {
        // load data
        GameData subregionGameData = GameDatabase.all(subregion).get(0);

        // create potentials map
        Map<String, GameDataList> keyPotentialsMap = new HashMap<>();
        GameDataList[][] mapOfPotentials = new GameDataList[size][];
        for (int x = 0; x < size; x++) {
            map[x] = new Terrain[size];
            mapOfPotentials[x] = new GameDataList[size];
        }

        // process map
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

                mapOfPotentials[indexX.get()][indexY.get()] = keyPotentialsMap.get(key);

                indexX.incrementAndGet();
            });

            indexY.decrementAndGet();
        });

        reducePotentials(mapOfPotentials);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                if (mapOfPotentials[x][y].size() > 0) {
                    GameData r = mapOfPotentials[x][y].getRandom(getScene().getRandom());

                    map[x][y] = new Terrain(getScene(), r, x, y);
                    addChild(map[x][y]);
                }
            }
        }
    }

    private GameDataList getPotentialsFromRule(GameData rule) {
        GameDataList list = new GameDataList();
        rule.ifHas("strict",
                s -> list.addAll(GameDatabase.all("terrain").stream()
                        .filter(d -> d.getString("name").equals(s.asString()))
                        .collect(Collectors.toList())),
                () -> list.addAll(GameDatabase.all("terrain").stream()
                        .filter(d -> d.getList("type").stream().anyMatch(t -> t.asString().equals(rule.getString("type"))))
                        .collect(Collectors.toList()))
        );

        return list;
    }

    int passes = 0;

    private void reducePotentials(GameDataList[][] mapOfPotentials) {
        boolean reduced = false;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                terrainListFactory.clear();
                terrainListFactory.setChoiceList(mapOfPotentials[x][y]);

                if (x != 0) {
                    terrainListFactory.setLeft(mapOfPotentials[x - 1][y]);
                }
                if (x < size - 1) {
                    terrainListFactory.setRight(mapOfPotentials[x + 1][y]);
                }
                if (y != 0) {
                    terrainListFactory.setBottom(mapOfPotentials[x][y - 1]);
                }
                if (y < size - 1) {
                    terrainListFactory.setTop(mapOfPotentials[x][y + 1]);
                }

                GameDataList newPotentials = terrainListFactory.getInstance();
                if (newPotentials.size() != mapOfPotentials[x][y].size()) {
                    reduced = true;
                }

                mapOfPotentials[x][y] = newPotentials;
            }
        }

        if (reduced) {
            passes++;
            reducePotentials(mapOfPotentials);
        } else {
            System.out.println(passes);
        }
    }

    private void mapRemove(Terrain[][] map, int x, int y) {
        if (map[x][y] != null) {
            getScene().removeGameObject(map[x][y]);
            map[x][y] = null;
        }
    }

    public Terrain getTerrainAt(float x, float y) {
        int ix = (int) x;
        int iy = (int) y;

        if (ix >= 0 && ix < size && iy >= 0 && iy < size)
            return map[ix][iy];
        else
            return null;
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return 0;
    }
}
