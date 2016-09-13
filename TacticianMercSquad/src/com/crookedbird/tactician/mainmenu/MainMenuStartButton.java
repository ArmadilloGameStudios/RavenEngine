package com.crookedbird.tactician.mainmenu;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.scene.Layer;
import com.crookedbird.engine.worldobject.WorldObject;

public class MainMenuStartButton extends WorldObject {
	public MainMenuStartButton(Layer parent) {
		super(parent, GameEngine.getEngine()
				.getFirstFromGameDatabase("StartScreen", new GameDataQuery() {
					public boolean matches(GameData row) {
						return row.getData("name") != null
								&& row.getData("name").isString()
								&& row.getData("name").getString()
										.equals("MainMenuStartButton");
					}

				}).getData("img"));

		setX((parent.getWidth() - getWidth()) / 2);
		setY(250);
	}
}
