package com.crookedbird.tactician.battle;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.worldobject.WorldObject;

public class TerrainHighlight extends WorldObject {
	public static class Color {
		static public String Blue = "Blue";
		static public String Red = "Red";
		static public String Green = "Green";
		static public String Yellow = "Yellow";
		static public String Magenta = "Magenta";
		static public String Cyan = "Cyan";
	}

	public TerrainHighlight(Terrain terrain, final String color) {
		super(terrain, GameEngine.getEngine().getFirstFromGameDatabase("TerrainHighlight", new GameDataQuery() {
			public boolean matches(GameData row) {
				return row.getData("Color").getString().equalsIgnoreCase(color);
			}
		}).getData("img"));
	}
}
