package com.crookedbird.tactician.battle.unit.action;

import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.Unit;

public abstract class UnitAction {
	private Unit unit;
	
	public UnitAction(Unit unit) {
		this.unit = unit;
	}

	public abstract String getColor();
	public abstract String getName();
	
	public abstract void setupAction();
	public abstract void doAction(Terrain t);	
	public abstract void cleanAction();	
	public abstract Unit getNextUnit();
	
	protected Unit getUnit() {
		return unit;
	}
}
