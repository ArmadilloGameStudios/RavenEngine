package com.crookedbird.tactician.battle.unit.action;

import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.Unit;

public class ActionNextUnit extends UnitAction {

	public ActionNextUnit(Unit unit) {
		super(unit);
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
	public Unit getNextUnit() {
		return getUnit().getNextUnit();
	}

	@Override
	public void setupAction() {
		getUnit().selectAction(null);

		getUnit().getTerrain().highlightOff();
		
		getUnit().getNextUnit().selectUnit();
	}
	
	@Override
	public void doAction(Terrain t) {

	}

	@Override
	public void cleanAction() {
		
	}
}
