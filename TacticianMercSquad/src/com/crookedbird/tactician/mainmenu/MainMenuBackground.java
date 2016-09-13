package com.crookedbird.tactician.mainmenu;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.scene.Layer;
import com.crookedbird.engine.worldobject.WorldObject;

public class MainMenuBackground extends WorldObject {
	public MainMenuBackground(Layer parent) {
		super(parent, GameEngine.getEngine()
				.getFirstFromGameDatabase("StartScreen", new GameDataQuery() {
					@Override
					public boolean matches(GameData row) {
						return row.getData("name") != null
								&& row.getData("name").isString()
								&& row.getData("name").getString()
										.equals("MainMenuBackground");
					}

				}).getData("img")); // , 0, 0, 1024, 768);
	}
}
