package com.crookedbird.tactician.battle;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.worldobject.WorldObject;

public class TerrainHighlight extends WorldObject {
	public static class Color {
		static String Blue = "Blue";
		static String Red = "Red";
	}

	public TerrainHighlight(Terrain terrain, final String color) {
		super(terrain, GameEngine.getEngine().getFromGameDatabase("TerrainHighlight", new GameDataQuery() {
			public boolean matches(GameData row) {
				return row.getData("Color").getString().equalsIgnoreCase(color);
			}
		}).getData("img"));
	}

}
