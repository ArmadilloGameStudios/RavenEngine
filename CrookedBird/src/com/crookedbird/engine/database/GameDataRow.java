package com.crookedbird.engine.database;

import java.util.ArrayList;
import java.util.List;

public class GameDataRow {
	private GameDataTable table;
	private List<Object> vals = new ArrayList<Object>();

	public GameDataRow(List<Object> vals, GameDataTable table) {
		this.table = table;
		this.vals = vals;
	}

	public List<Object> getValues() {
		return vals;
	}

	public void set(Object o, int c) {
		vals.set(c, o);
	}

	public Object get(int c) {
		return vals.get(c);
	}

	public Object get(String name) {
		int c = table.getColumnIndex(name);

		return vals.get(c);
	}
}
