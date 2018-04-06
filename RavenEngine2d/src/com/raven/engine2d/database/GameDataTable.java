package com.raven.engine2d.database;

import java.util.ArrayList;
import java.util.List;

public class GameDataTable extends com.raven.engine2d.database.GameDataList {
	private String name;

	public GameDataTable(String name) {
		this.name = name;
	}

	public <G extends com.raven.engine2d.database.GameDatable> GameDataTable(String name, List<G> list) {
        super(list);
        this.name = name;
    }

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
	    List<String> lines = new ArrayList<>();


	    for (com.raven.engine2d.database.GameData gameData : this) {
            lines.add(gameData.toString());
        }

        String f = String.join(",\n", lines);

        return f;
    }
}
