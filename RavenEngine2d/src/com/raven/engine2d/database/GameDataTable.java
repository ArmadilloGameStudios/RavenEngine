package com.raven.engine2d.database;

import java.util.ArrayList;
import java.util.List;

public class GameDataTable extends GameDataList {
	private String name;

	public GameDataTable(String name) {
		this.name = name;
	}

	public <G extends GameDatable> GameDataTable(String name, List<G> list) {
        super(list);
        this.name = name;
    }

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
	    List<String> lines = new ArrayList<>();


	    for (GameData gameData : this) {
            lines.add(gameData.toString());
        }

        String f = String.join(",\n", lines);

        return f;
    }
}