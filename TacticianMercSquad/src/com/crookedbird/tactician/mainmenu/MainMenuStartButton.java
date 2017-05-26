package com.crookedbird.tactician.mainmenu;

import com.crookedbird.tactician.battle.BattleScene;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.scene.Layer;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.WorldObject;

public class MainMenuStartButton extends WorldObject {

	private static GameData getGameData() {
		return GameEngine.getEngine()
				.getFirstFromGameDatabase("StartScreen", new GameDataQuery() {
					public boolean matches(GameData row) {
						return row.getData("name") != null
								&& row.getData("name").isString()
								&& row.getData("name").getString()
										.equals("MainMenuStartButton");
					}

				}).getData("model");
	}
	
	public MainMenuStartButton(Layer parent) {
		super(parent, getGameData());


		addMouseHandler(new MouseHandler() {
			@Override
			public void onMouseClick() {
				GameEngine.getEngine().getGame().prepTransitionScene(new BattleScene(GameEngine.getEngine().getGame()));
			}

			@Override
			public void onMouseEnter() {
				setAnimationState("hover");
			}

			@Override
			public void onMouseLeave() {
				setAnimationState("idle");
			}

			@Override
			public void onMouseMove() {

			}
		});
	}
}
