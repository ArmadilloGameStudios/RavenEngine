package com.crookedbird.tactician.mainmenu;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.scene.Layer;
import com.crookedbird.engine.worldobject.WorldObject;

public class MainMenuBackground extends WorldObject {
	public MainMenuBackground(Layer parent) {
		super(parent, GameEngine.getEngine().getFromGameDatabase("Anim",
				"Name", "MainMenuBackground"), 0, 0, 1024, 768);
	}
}
