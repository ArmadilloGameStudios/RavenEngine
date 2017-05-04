package com.crookedbird.tactician.mainmenu;

import com.raven.engine.Game;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.worldobject.MouseHandler;
import com.crookedbird.tactician.battle.BattleScene;

public class MainMenuScene extends Scene {
	private Game game;

	private Layer backgroundLayer;

	private MainMenuBackground bg;
	private MainMenuStartButton sb;

	public MainMenuScene(Game game) {
		super(game);
		this.game = game;
	}

	@Override
	public void enterScene() {
		addLayer(backgroundLayer = new Layer(this));

		bg = new MainMenuBackground(backgroundLayer);
		sb = new MainMenuStartButton(backgroundLayer);

		backgroundLayer.addChild(bg);
		backgroundLayer.addChild(sb);
	}

	@Override
	public void exitScene() {
		this.getLayers().clear();
	}
}
