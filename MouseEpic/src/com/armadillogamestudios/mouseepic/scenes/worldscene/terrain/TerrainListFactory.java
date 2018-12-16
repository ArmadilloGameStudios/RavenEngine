package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.util.Factory;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerrainListFactory extends Factory<GameDataList> {

    private GameDataList left, right, bottom, top;
    private GameDataList choiceList;

    public void setChoiceList(GameDataList list) {
        choiceList = list;
    }

    public void setLeft(GameDataList left) {
        this.left = left;
    }

    public void setRight(GameDataList right) {
        this.right = right;
    }

    public void setBottom(GameDataList bottom) {
        this.bottom = bottom;
    }

    public void setTop(GameDataList top) {
        this.top = top;
    }

    @Override
    public GameDataList getInstance() {
        Stream<GameData> stream = choiceList.stream();

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

        return stream.collect(Collectors.toCollection(GameDataList::new));
    }

    private Stream<GameData> filterStream(Stream<GameData> stream, GameDataList neighbor, String side, String neighborSide) {
        return stream.filter(terrain -> neighbor.stream().anyMatch(n -> filter(terrain.getData(side), n.getData(neighborSide))));
    }

    private boolean filter(GameData a, GameData b) {
        if (a.isString()) {
            String aString = a.asString();

            if (b.isString()) {
                String bString = b.asString();

                return aString.equals(bString);
            } else {
                GameDataList bList = b.asList();

                return bList.stream().anyMatch(s -> s.asString().equals(aString));
            }
        } else {
            GameDataList aList = a.asList();

            if (b.isString()) {
                String bString = b.asString();

                return aList.stream().anyMatch(s -> s.asString().equals(bString));
            } else {
                GameDataList bList = b.asList();

                return aList.stream().anyMatch(s1 -> bList.stream().anyMatch(s2 -> s1.asString().equals(s2.asString())));
            }
        }
    }

    @Override
    public void clear() {
        left = null;
        bottom = null;
        right = null;
        top = null;
        choiceList = null;
    }
}
