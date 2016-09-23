package com.crookedbird.tactician.player;

import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.action.UnitAction;

public abstract class Player {
	private int team;
	private BattleScene battleScene;
	
	public Player(BattleScene battleScene, int team) {
		this.team = team;
		this.battleScene = battleScene;
	}

	public abstract boolean isHuman();
	public abstract UnitAction selectAction(List<UnitAction> unitActions);
	public abstract Terrain selectTerrain(UnitAction unitAction);
}
