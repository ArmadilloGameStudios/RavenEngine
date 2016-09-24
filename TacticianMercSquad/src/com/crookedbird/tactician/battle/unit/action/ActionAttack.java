package com.crookedbird.tactician.battle.unit.action;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.Unit;

public class ActionAttack extends UnitAction {
	private List<Terrain> actionTerrain = new ArrayList<Terrain>();
	private boolean kill;
	private Terrain attackedTerrain;
	private float tickTotal, attackTotal;
	private int totalLength;

	public ActionAttack(Unit unit, BattleScene bs) {
		super(unit, bs);
	}

	@Override
	public String getColor() {
		return TerrainHighlight.Color.Red;
	}

	@Override
	public String getName() {
		return "Attack";
	}

	@Override
	public void setupAction() {
		getUnit().selectAction(this);

		getUnit().getTerrain().highlight(TerrainHighlight.Color.Yellow);

		if (getUnit().getStats().getCurrentStamina() >= stmCost()) {
			actionTerrain = getTerrain();

			for (Terrain at : actionTerrain) {
				at.setAction(this);
			}
		}
	}

	@Override
	public List<Terrain> getTerrain() {
		List<Terrain> terrain = new ArrayList<Terrain>();

		Terrain t;
		t = getUnit().getRelitiveTerrain(1, 0);
		if (validateTerrain(t)) {
			terrain.add(t);
		}

		t = getUnit().getRelitiveTerrain(-1, 0);
		if (validateTerrain(t)) {
			terrain.add(t);
		}

		t = getUnit().getRelitiveTerrain(0, 1);
		if (validateTerrain(t)) {
			terrain.add(t);
		}

		t = getUnit().getRelitiveTerrain(0, -1);
		if (validateTerrain(t)) {
			terrain.add(t);
		}

		return terrain;
	}

	@Override
	public void startAction(Terrain t) {
		tickTotal = 0F;
		totalLength = getUnit().setAnimationState("attack");
		attackedTerrain = t;
		kill = false;
	}

	@Override
	public boolean tickAction(float deltaTime) {
		if (tickTotal < totalLength && tickTotal + deltaTime > totalLength
				&& !kill) {
			this.getUnit().setAnimationState("idle");

			kill = attackedTerrain.getUnit().interaction().damage(8);
			
			if (kill)
				totalLength += attackedTerrain.getUnit().setAnimationState(
						"die");
			else
				return true;
		} else if (tickTotal + deltaTime > totalLength) {
			attackedTerrain.getUnit().kill();
			return true;
		}

		tickTotal += deltaTime;

		return false;
	}

	@Override
	public int stmCost() {
		return getUnit().getStats().getWeight() + 2;
	}

	@Override
	public void cleanAction() {
		getUnit().getTerrain().highlightOff();
		for (Terrain t : actionTerrain) {
			t.removeAction();
		}
		actionTerrain.clear();
	}

	private boolean validateTerrain(Terrain t) {
		return t != null && t.getUnit() != null
				&& t.getUnit().getTeam() != getUnit().getTeam();
	}

	@Override
	public ActionType getType() {
		return ActionType.ATTACK;
	}

	@Override
	public boolean canAttack() {
		return getTerrain().size() != 0;
	}
}
