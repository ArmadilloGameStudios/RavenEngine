package com.crookedbird.tactician.battle.sidebar;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataQuery;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.TextObject;
import com.raven.engine.worldobject.WorldObject;

public class Holder extends WorldObject {
	private static final int baseline = 28;
	
	private TextObject des, val;

	public Holder(Parentable parent, int x, int y) {
		super(parent, GameEngine.getEngine().getFirstFromGameDatabase("BattleScreen", new GameDataQuery() {
			public boolean matches(GameData row) {
				return row.getData("Name").getString().equalsIgnoreCase("StatHolder");
			}
		}).getData("img"), x, y);

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
