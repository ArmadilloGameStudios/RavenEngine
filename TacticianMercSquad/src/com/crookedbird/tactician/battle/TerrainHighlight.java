package com.crookedbird.tactician.battle;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameDataRow;
import com.crookedbird.engine.worldobject.Parentable;
import com.crookedbird.engine.worldobject.WorldObject;

public class TerrainHighlight extends WorldObject {
	public static class Color {
		static String Blue = "Blue";
	}

	public TerrainHighlight(Terrain terrain, String color) {
		super(terrain, GameEngine.getEngine().getFromGameDatabase("Anim", "Name", "TerrainHighlight" + color), 0, 0, 16,
				16);

	}

}
