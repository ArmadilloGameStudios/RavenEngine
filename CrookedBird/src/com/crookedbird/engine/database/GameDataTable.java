package com.crookedbird.engine.database;

import java.util.ArrayList;
import java.util.List;

public class GameDataTable {
	private List<GameData> data = new ArrayList<GameData>();
	private String name;

	public GameDataTable(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public GameData get(GameDataQuery query) {
		for (GameData row : data) {
			if (query.matches(row)) {
				return row;
			}
		}
		return null;
	}

	public List<GameData> getRows() {
		return data;
	}

	public int getRowCount() {
		return data.size();
	}

	public void addRow(GameData val) {
		data.add(val);
	}

	public void deleteRow(int row) {
		data.remove(row);
	}
}
