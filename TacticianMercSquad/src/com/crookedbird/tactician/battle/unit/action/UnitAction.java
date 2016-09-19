package com.crookedbird.tactician.battle.unit.action;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.Unit;

public abstract class UnitAction {
	private Unit unit;
	private BattleScene battleScene;
	
	public UnitAction(Unit unit, BattleScene bs) {
		this.unit = unit;
		battleScene = bs;
	}

	public abstract String getColor();
	public abstract String getName();
	
	public abstract void setupAction();
	public abstract void startAction(Terrain t);
	public abstract boolean tickAction(float deltaTime);
	public abstract void cleanAction();	
	

	public abstract int stmCost();
	// public abstract Unit getNextUnit();
	
	protected Unit getUnit() {
		return unit;
	}

	protected BattleScene getBattleScene() {
		return battleScene;
	}
}
