package com.crookedbird.tactician.battle;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;

public class TerrainQuery extends GameDataQuery {
	private String theme;
	private List<String> type;

	public TerrainQuery(String theme, List<String> type) {
		this.theme = theme;
		this.type = new ArrayList<String>();

		for (String t : type) {
			this.type.add(t.toLowerCase());
		}
	}

	@Override
	public boolean matches(GameData row) {
		GameData data = row.getData("type");

		if (data.isList()) {
			if (type.size() == data.getList().size()) {
				boolean suc = true;

				for (int i = 0; i < type.size() && suc; i++) {
					suc &= type.contains(data.getList().get(i).getString()
							.toLowerCase());
				}

				return suc;
			}
		} else if (data.isString()) {
			if (type.size() == 1)
				return data.getString().equalsIgnoreCase(type.get(0));
		}

		return false;
	}
}
