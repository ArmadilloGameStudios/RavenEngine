package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.util.Factory;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerrainFactory extends Factory<Terrain> {

    private GameDataList terrainList;
    private WorldScene scene;
    private Terrain left, right, bottom, top;
    private int x, y;

    public TerrainFactory(WorldScene scene, GameDataList terrainList) {
        this.terrainList = terrainList;
        this.scene = scene;
    }

    @Override
    public Terrain getInstance() {
        Stream<GameData> stream = terrainList.stream();

        if (left != null) {
            stream = filterStream(stream, left, "left", "right");
        }

        if (right != null) {
            stream = filterStream(stream, right, "right", "left");
        }

        if (bottom != null) {
            stream = filterStream(stream, bottom, "bottom", "top");
        }

        if (top != null) {
            stream = filterStream(stream, top, "top", "bottom");
        }

        GameDataList results = stream.collect(Collectors.toCollection(GameDataList::new));

        if (results.size() > 0)
            return new Terrain(scene, results.getRandom(scene.getRandom()), x, y);
        else
            return null;
    }

    private Stream<GameData> filterStream(Stream<GameData> stream, Terrain terrain, String from, String to) {
        return stream.filter(t -> {
            GameData compareToData = terrain.getGameData().getData(to);
            GameData compareFromData = t.getData(from);

            if (compareToData.isString()) {
                String compareToString = compareToData.asString();

                if (compareFromData.isString()) {
                    String compareFromString = compareFromData.asString();

                    return compareFromString.equals(compareToString);
                } else {
                    GameDataList compareFromList = compareFromData.asList();

                    return compareFromList.stream().anyMatch(c -> compareToString.equals(c.asString()));
                }
            } else {
                GameDataList compareToList = compareToData.asList();

                if (compareFromData.isString()) {
                    String compareFromString = compareFromData.asString();

                    return compareToList.stream().anyMatch(c -> compareFromString.equals(c.asString()));
                } else {
                    GameDataList compareFromList = compareFromData.asList();

                    return compareFromList.stream().anyMatch(c1 -> compareToList.stream().anyMatch(c2 -> c2.asString().equals(c1.asString())));
                }
            }
        });
    }

    @Override
    public void clear() {
        left = null;
        bottom = null;
        x = 0;
        y = 0;
    }

    public void setLeft(Terrain left) {
        this.left = left;
    }

    public void setRight(Terrain right) {
        this.right = right;
    }

    public void setBottom(Terrain bottom) {
        this.bottom = bottom;
    }

    public void setTop(Terrain top) {
        this.top = top;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
