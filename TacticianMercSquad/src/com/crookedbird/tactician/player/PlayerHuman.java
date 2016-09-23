package com.crookedbird.tactician.player;

import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.action.UnitAction;

public class PlayerHuman extends Player {

	public PlayerHuman(BattleScene battleScene, int team) {
		super(battleScene, team);
	}

	@Override
	public boolean isHuman() {
		return true;
	}

	@Override
	public UnitAction selectAction(List<UnitAction> unitActions) {
		return null;
	}

	@Override
	public Terrain selectTerrain(UnitAction unitAction) {
		return null;
	}
}
