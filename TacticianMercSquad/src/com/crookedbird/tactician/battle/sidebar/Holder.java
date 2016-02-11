package com.crookedbird.tactician.battle.sidebar;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.worldobject.TextObject;
import com.crookedbird.engine.worldobject.WorldObject;

public class Holder extends WorldObject {

	public Holder(Sidebar parent, int x, int y) {
		super(parent, GameEngine.getEngine().getGameDatabase().getTable("Anim")
				.get("Name", "SidebarStatHolder"), x, y, 192, 64);

		setText(new TextObject("Cat"));
	}
}
