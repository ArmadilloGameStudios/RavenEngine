package com.crookedbird.tactician.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.unit.Unit;
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

		// Attack
		for (UnitAction action : sortedActions.get(ActionType.ATTACK)) {
			if (action.canAttack()) {
				if (action.stmCost() <= action.getUnit().getStats()
						.getCurrentStamina())
					return action;
				else
					return unitActions.get(2);
			}
		}

		// Move
		for (UnitAction action : sortedActions.get(ActionType.MOVE)) {
			if (action.stmCost() <= action.getUnit().getStats()
					.getCurrentStamina()
					&& action.getTerrain().size() > 0) {
				return action;
			}
		}

		// End Turn
		return unitActions.get(2);
	}

	@Override
	public Terrain selectTerrain(UnitAction unitAction) {
		List<Terrain> terrain = unitAction.getTerrain();

		List<Unit> foes = getBattleScene().getOtherTeamUnits(
				unitAction.getUnit().getTeam());

		Unit unit = unitAction.getUnit();
		Unit closetFoe = null;

		int dist = 0;

		for (Unit foe : foes) {
			int d = unit.getDistance(foe);
			if ((d < dist && d != 0) || dist == 0) {
				dist = d;
				closetFoe = foe;
			} else if (d == dist && d != 0) {
				if (closetFoe.getStats().getCurrentHitPoints() > foe.getStats()
						.getCurrentHitPoints()) {
					closetFoe = foe;
				}
			}
		}

		Terrain t = null;

		if (closetFoe != null)
			t = closetFoe.closestTerrain(terrain);

		if (t == null && terrain.size() > 0) {
			t = terrain.get(new Random().nextInt(terrain.size()));
		}

		return t;
	}
}
