package com.raven.engine2d.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cookedbird on 11/15/17.
 */
public class GameDataList extends ArrayList<GameData> {
    static Random rand = new Random();

    public GameDataList() {
        super();
    }

    public <G extends GameDatable> GameDataList(List<G> list) {
        super();

        for (GameDatable datable : list) {
            this.add(datable.toGameData());
        }
    }

    public GameData queryFirst(GameDataQuery query) {
        for (GameData row : this) {
            if (query.matches(row)) {
                return row;
            }
        }
        return null;
    }

    public GameDataList queryAll(GameDataQuery query) {
        GameDataList l = new GameDataList();

        for (GameData row : this) {
            if (query.matches(row)) {
                l.add(row);
            }
        }
        return l;
    }

    public GameData getRandom() {
        int totalWeight = 0;
        for (GameData i : this)
        {
            totalWeight += getWeight(i);
        }

        int randomIndex = -1;
        double random = Math.random() * totalWeight;
        for (int i = 0; i < size(); ++i)
        {
            random -= getWeight(get(i));
            if (random <= 0.0d)
            {
                randomIndex = i;
                break;
            }
        }

        return get(randomIndex);
    }

    public GameData queryRandom(GameDataQuery gameDataQuery) {
        return queryAll(gameDataQuery).getRandom();
    }

    private int getWeight(GameData data) {
        AtomicInteger weight = new AtomicInteger();

        data.ifHas("weight",
                w -> weight.set(w.asInteger()),
                () -> weight.set(1));

        return weight.get();
    }
}
