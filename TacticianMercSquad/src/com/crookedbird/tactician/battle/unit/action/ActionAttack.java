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
	private float tickTotal;

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

		if (getUnit().getStats().getCurrentStamina() > 0) {
			Terrain t;
			t = getUnit().getRelitiveTerrain(1, 0);
			if (validateTerrain(t)) {
				actionTerrain.add(t);
			}

			t = getUnit().getRelitiveTerrain(-1, 0);
			if (validateTerrain(t)) {
				actionTerrain.add(t);
			}

			t = getUnit().getRelitiveTerrain(0, 1);
			if (validateTerrain(t)) {
				actionTerrain.add(t);
			}

			t = getUnit().getRelitiveTerrain(0, -1);
			if (validateTerrain(t)) {
				actionTerrain.add(t);
			}

			for (Terrain at : actionTerrain) {
				at.setAction(this);
			}
		}
	}

	@Override
	public void startAction(Terrain t) {
		tickTotal = 0F;
		attackedTerrain = t;
		
		kill = t.getUnit().interaction().damage(12);
		if (kill) 
			t.getUnit().setAnimationState("die");
	}

	@Override
	public boolean tickAction(float deltaTime) {
		if (kill) {
			if (tickTotal + deltaTime > 300F) {
				attackedTerrain.getUnit().kill();
				return true;
			}
			
			tickTotal += deltaTime;
			
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int stmCost() {
		return getUnit().getStats().getWeight();
	}

	@Override
	public void cleanAction() {
		for (Terrain t : actionTerrain) {
			t.removeAction();
		}
		actionTerrain.clear();
	}

	private boolean validateTerrain(Terrain t) {
		return t != null && t.getUnit() != null
				&& t.getUnit().getTeam() != getUnit().getTeam();
	}
}
