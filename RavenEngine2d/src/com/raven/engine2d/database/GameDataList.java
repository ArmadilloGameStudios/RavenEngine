package com.raven.engine2d.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by cookedbird on 11/15/17.
 */
public class GameDataList extends ArrayList<com.raven.engine2d.database.GameData> {
    static Random rand = new Random();

    public GameDataList() {
        super();
    }

    public <G extends com.raven.engine2d.database.GameDatable> GameDataList(List<G> list) {
        super();

        for (com.raven.engine2d.database.GameDatable datable : list) {
            this.add(datable.toGameData());
        }
    }

    public com.raven.engine2d.database.GameData queryFirst(GameDataQuery query) {
        for (com.raven.engine2d.database.GameData row : this) {
            if (query.matches(row)) {
                return row;
            }
        }
        return null;
    }

    public GameDataList queryAll(GameDataQuery query) {
        GameDataList l = new GameDataList();

        for (com.raven.engine2d.database.GameData row : this) {
            if (query.matches(row)) {
                l.add(row);
            }
        }
        return l;
    }

    public com.raven.engine2d.database.GameData getRandom() {
        return get((int)(rand.nextFloat() * size()));
    }

    public com.raven.engine2d.database.GameData queryRandom(GameDataQuery gameDataQuery) {
        return queryAll(gameDataQuery).getRandom();
    }
}
