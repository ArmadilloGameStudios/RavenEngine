package com.crookedbird.tactician.battle.unit.action;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.Unit;

public class ActionNextUnit extends UnitAction {

	public ActionNextUnit(Unit unit, BattleScene bs) {
		super(unit, bs);
	}

	@Override
	public String getColor() {
		return TerrainHighlight.Color.Yellow;
	}

	@Override
	public String getName() {
		return "End Turn";
	}

	@Override
	public void setupAction() {
		getUnit().selectAction(null);
		
		getUnit().getTerrain().highlightOff();
		
		getBattleScene().getNextSelectedUnit().interaction().recover();
		getBattleScene().getNextSelectedUnit().selectUnit();
	}
	
	@Override
	public boolean tickAction(float deltaTime) {
		return true;
	}
	
	@Override
	public void startAction(Terrain t) {
		setupAction();
	}

	@Override
	public int stmCost() {
		return 0;
	}

	@Override
	public void cleanAction() {
		
	}

	@Override
	public ActionType getType() {
		return ActionType.END_TURN;
	}

	@Override
	public List<Terrain> getTerrain() {
		List<Terrain> terrain = new ArrayList<Terrain>();
		terrain.add(getUnit().getTerrain());
		
		return terrain;
	}
}
