package com.crookedbird.tactician.battle;

import com.raven.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.unit.Unit;

public class Level extends WorldObject {
	private Terrain[][] terrain;
	private BattleScene bs;

	public Level(BattleScene bs) {
		super(bs.getLevelLayer(), null);

		this.bs = bs;
		final Level l = this;
	}

	public void setTerrain(Terrain[][] terrain) {
		this.terrain = terrain;

		for (int i = 0; i < terrain.length; i++) {
			for (int j = 0; j < terrain[i].length; j++) {
				this.addChild(terrain[i][j]);
			}
		}
	}

	@Override
	public int getWidth() {
		return 32 * 16;
	}

	@Override
	public int getHeight() {
		return 32 * 16;
	}
	
	public Unit getSelectedUnit() {
		return bs.getSelectedUnit();
	}

	public Terrain[][] getTerrain() {
		return terrain;
	}

	public BattleSceneState getState() {
		return bs.getState();
	}
	
	public void onUpdate(double deltaTime) {
		if (bs.getState() == BattleSceneState.EXECUTING_ACTION) {
			// bs.setState(BattleSceneState.UNIT_SELECTION);
		}
	}
}
