package com.crookedbird.tactician.battle.unit;

public class UnitAction {
	private String type = "Move";
	private Unit unit;
	
	public UnitAction(Unit unit) {
		this.unit = unit;
	}
	
	public void doAction(int x, int y) {
		doMove(x, y);
	}
	
	private void doMove(int x, int y) {
		unit.getTerrain().setUnit(null);
		unit.setTerrain(x, y);
		unit.getTerrain().setUnit(unit);
	}
}
