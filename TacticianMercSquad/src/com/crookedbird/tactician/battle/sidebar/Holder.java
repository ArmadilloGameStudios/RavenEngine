package com.crookedbird.tactician.battle.sidebar;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.worldobject.TextObject;
import com.crookedbird.engine.worldobject.WorldObject;

public class Holder extends WorldObject {
	private static final int baseline = 28;
	
	private TextObject des, val;

	public Holder(Sidebar parent, int x, int y) {
		super(parent, GameEngine.getEngine().getGameDatabase().getTable("Anim")
				.get("Name", "SidebarStatHolder"), x, y, 192, 64);

		addText(des = new TextObject());
		des.setY(baseline);
		des.setX(25);
		
		addText(val = new TextObject());
		val.setY(baseline);
		val.setX(100);
	}
	
	public void setDescription(String des) {
		this.des.setText(des);
	}

	public void setValue(String val) {
		this.val.setText(val);
	}
}
