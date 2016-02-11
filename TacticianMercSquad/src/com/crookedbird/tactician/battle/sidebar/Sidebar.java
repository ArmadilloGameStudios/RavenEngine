package com.crookedbird.tactician.battle.sidebar;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.worldobject.Parentable;
import com.crookedbird.engine.worldobject.WorldObject;

public class Sidebar extends WorldObject {
	static class Stats {
		int Movement = 0;
		int Initiative = 1;
		int Accuracy = 2;
		int Dodge = 3;
		int Stamina= 4;
		int HitPoints = 5;
		int Resistance = 6;
		int Absorption = 7;
	}
	
	private List<Holder> holders = new ArrayList<Holder>();
	
	public Sidebar(Parentable parent) {
		super(parent, null);
		
		for (int i = 0; i < 8; i++) {
			Holder h = new Holder(this, 0, 64 * i);
			holders.add(h);
			this.addChild(h);
		}
		
		
	}

}
