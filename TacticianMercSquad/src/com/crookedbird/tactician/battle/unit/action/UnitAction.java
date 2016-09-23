package com.crookedbird.tactician.battle.unit.action;

import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.Unit;

public abstract class UnitAction {
	public static enum ActionType {
		MOVE, ATTACK, END_TURN
	}
	
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

	public abstract List<Terrain> getTerrain();
	
	public abstract ActionType getType();

	public abstract int stmCost();
	// public abstract Unit getNextUnit();
	
	public Unit getUnit() {
		return unit;
	}

	public BattleScene getBattleScene() {
		return battleScene;
	}

	public boolean canAttack() {
		return false;
	}
}
