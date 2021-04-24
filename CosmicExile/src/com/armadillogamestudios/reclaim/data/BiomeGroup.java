package com.armadillogamestudios.reclaim.data;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.database.GameDatable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class BiomeGroup implements GameDatable {

    private List<Integer> range = new ArrayList<>();
    private List<String> biomes;

    public BiomeGroup(GameData gameData) {
        List<Integer> percent = gameData.getList("range").stream().map(GameData::asInteger).collect(Collectors.toList());

        AtomicInteger current = new AtomicInteger();
        percent.forEach(p -> {
            current.addAndGet(p);

            range.add(current.get());
        });

        biomes = gameData.getList("biome").stream().map(GameData::asString).collect(Collectors.toList());
    }

    public Biome get(int biomeValue) {

        GameDataTable biomeTable = GameDatabase.all("biome");

        for (int i = 0; i < range.size(); i++) {
            if (biomeValue < range.get(i)) {

                return new Biome(biomeTable.query("name", biomes.get(i)));
            }
        }

        return new Biome(biomeTable.query("name", biomes.get(biomes.size() - 1)));
    }

    @Override
    public GameData toGameData() {
        return null;
    }
}
