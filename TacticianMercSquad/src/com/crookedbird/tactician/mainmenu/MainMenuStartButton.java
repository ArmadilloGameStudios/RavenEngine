package com.crookedbird.tactician.mainmenu;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.scene.Layer;
import com.crookedbird.engine.worldobject.WorldObject;

public class MainMenuStartButton extends WorldObject {
	public MainMenuStartButton(Layer parent) {
		super(parent, GameEngine.getEngine().getFromGameDatabase("Anim",
				"Name", "MainMenuStartButton"));

		setX((parent.getWidth() - getWidth()) / 2);
		setY(250);
	}
}
