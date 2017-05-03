package com.crookedbird.tactician.mainmenu;

import com.raven.engine.Game;
import com.raven.engine.input.MouseClickInput;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.worldobject.ClickHandler;
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
		sb.addClickHandler(new ClickHandler() {
			@Override
			public void onMouseClick(MouseClickInput m) {
				game.prepTransitionScene(new BattleScene(game));
			}
		});

		backgroundLayer.addChild(bg);
		backgroundLayer.addChild(sb);
	}

	@Override
	public void exitScene() {
		this.getLayers().clear();
	}
}
