package com.crookedbird.tactician.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.action.UnitAction;
import com.crookedbird.tactician.battle.unit.action.UnitAction.ActionType;

public class PlayerAI extends Player {

	public PlayerAI(BattleScene battleScene, int team) {
		super(battleScene, team);
	}

	@Override
	public boolean isHuman() {
		return false;
	}

	@Override
	public UnitAction selectAction(List<UnitAction> unitActions) {
		Map<ActionType, List<UnitAction>> sortedActions = new HashMap<ActionType, List<UnitAction>>();

		for (ActionType type : ActionType.values()) {
			sortedActions.put(type, new ArrayList<UnitAction>());
		}

		for (UnitAction action : unitActions) {
			sortedActions.get(action.getType()).add(action);
		}

		for (UnitAction action : sortedActions.get(ActionType.ATTACK)) {
			if (action.stmCost() < action.getUnit().getStats()
					.getCurrentStamina()
					&& action.canAttack())
				return action;
		}

		for (UnitAction action : sortedActions.get(ActionType.MOVE)) {
			if (action.stmCost() < action.getUnit().getStats()
					.getCurrentStamina())
				return action;
		}

		return unitActions.get(2);
	}

	@Override
	public Terrain selectTerrain(UnitAction unitAction) {
		List<Terrain> terrain = unitAction.getTerrain();

		int pick = new Random().nextInt(terrain.size());

		return terrain.get(pick);
	}
}
