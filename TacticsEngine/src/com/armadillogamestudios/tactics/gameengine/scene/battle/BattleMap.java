package com.armadillogamestudios.tactics.gameengine.scene.battle;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

import java.util.Random;

public class BattleMap<S extends BattleScene<?>> extends WorldObject<S, WorldObject<S, ?>> {

    private int size = 26;

    GameData settinggd;
    GameData tileset;

    private Tile<S>[][] tiles = new Tile[size][];

    private final Random random;

    public BattleMap(S scene, long mapSeed, String setting) {
        super(scene);

        random = new Random(mapSeed);

        // gen
        loadData(setting);
        createMap();

        // center
    }

    private void loadData(String setting) {
        settinggd = GameDatabase.all("map").query("setting", setting);
        tileset = GameDatabase.all("tileset").query("name", settinggd.getString("tileset"));
    }

    private void createMap() {

        // create group map
        String[][] groupMap = new String[size][];

        for (int x = 0; x < size; x++) {
            tiles[x] = new Tile[size];

            groupMap[x] = new String[size];

            for (int y = 0; y < size; y++) {
                groupMap[x][y] = "ground";
            }
        }

        // edges
        for (int i = 0; i < size; i++) {
            groupMap[0][i] = "edge";
            groupMap[1][i] = "edge";
            groupMap[size - 1][i] = "edge";
            groupMap[size - 2][i] = "edge";

            groupMap[i][0] = "edge";
            groupMap[i][1] = "edge";
            groupMap[i][size - 1] = "edge";
            groupMap[i][size - 2] = "edge";
        }

        // TODO structures

        //populate tiles
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile<S> tile = tiles[x][y] = new Tile<>(getScene(), tileset.getData(groupMap[x][y]));
                tile.setX(x);
                tile.setY(y);
            }
        }
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .1f;
    }
}
