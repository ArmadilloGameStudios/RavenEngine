package com.crookedbird.tactician.battle.unit;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.util.Pair;
import com.crookedbird.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.BattleSceneState;
import com.crookedbird.tactician.battle.Level;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.action.ActionMove;
import com.crookedbird.tactician.battle.unit.action.ActionNextUnit;
import com.crookedbird.tactician.battle.unit.action.UnitAction;

public class Unit {
	private WorldObject unitWorldObject;
	private BattleScene battleScene;
	private Level level;
	private Terrain terrain;
	private int tx, ty;
	private int team;
	private List<UnitAction> unitActions = new ArrayList<UnitAction>();
	private UnitAction selectedAction;
	private UnitStats stats;

	public Unit(BattleScene battleScene, Terrain terrain, final int team) {
		unitWorldObject = new UnitWorldObject(this, battleScene.getLevel(),
				GameEngine.getEngine()
						.getFirstFromGameDatabase("Units", new GameDataQuery() {
							public boolean matches(GameData row) {
								switch (team) {
								case 1:
									return row.getData("team").getString()
											.equalsIgnoreCase("red");
								case 2:
								default:
									return row.getData("team").getString()
											.equalsIgnoreCase("blue");
								}
							}
						}).getData("img"), terrain.getX(), terrain.getY(), 16,
				16);

		this.tx = terrain.getGridX();
		this.ty = terrain.getGridY();
		this.terrain = terrain;
		terrain.setUnit(this);
		this.team = team;
		int stm = range(20, 40);
		this.stats = new UnitStats(range(2, 4), // mov
				range(4, 40), // init
				0, // acc
				0, // dge
				stm, stm, // stm
				range(4, 12), // rec
				20, 20, // hps
				0, // res
				range(5, 20) // wgt
		);
		this.battleScene = battleScene;
		this.level = battleScene.getLevel();

		this.unitActions.add(new ActionMove(this));
		this.unitActions.add(new ActionNextUnit(this));
	}

	private int range(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min)) + min;
	}

	public WorldObject getWorldObject() {
		return unitWorldObject;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(int tx, int ty) {
		unitWorldObject.setX(tx * 16);
		unitWorldObject.setY(ty * 16);
		this.tx = tx;
		this.ty = ty;
		this.terrain = level.getTerrain()[tx][ty];
	}

	public void setTerrain(Terrain t) {
		unitWorldObject.setX(t.getGridX() * 16);
		unitWorldObject.setY(t.getGridY() * 16);
		this.tx = t.getGridX();
		this.ty = t.getGridY();
		this.terrain = t;
	}

	public Terrain getRelitiveTerrain(int offsetX, int offsetY) {
		Terrain[][] t = level.getTerrain();

		if (tx + offsetX >= 0 && tx + offsetX < t.length) {
			if (ty + offsetY >= 0 && ty + offsetY < t[tx + offsetX].length) {
				return t[tx + offsetX][ty + offsetY];
			}
		}

		return null;
	}

	public int getGridX() {
		return tx;
	}

	public int getGridY() {
		return ty;
	}

	Level getLevel() {
		return level;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public UnitStats getStats() {
		return stats;
	}

	public void recover() {
		int cs = stats.getRecovery() + stats.getCurrentStamina();

		if (cs > stats.getMaxStamina()) {
			cs = stats.getMaxStamina();
		}

		stats.setCurrentStamina(cs);
	}

	public Unit getNextUnit() {
		return battleScene.getNextSelectedUnit();
	}

	public void selectUnit() {
		battleScene.setSelectedUnit(this);
		battleScene.setState(BattleSceneState.ACTION_SELECTION);
		battleScene.setUnitAction(this.unitActions);

		getTerrain().highlight(TerrainHighlight.Color.Yellow);
	}

	public void selectAction(UnitAction action) {
		if (selectedAction != null)
			selectedAction.cleanAction();

		selectedAction = action;

		battleScene.setState(BattleSceneState.ACTION_TARGET_SELECTION);
	}

	public void executeAction(int x, int y) {
		Terrain oldTerrain = getTerrain();
		
		if (level.getTerrain()[x][y].hasAction()) {
			battleScene.setState(BattleSceneState.EXECUTING_ACTION);

			selectedAction.doAction(level.getTerrain()[x][y]);
			selectedAction.cleanAction();
			oldTerrain.highlightOff();
			getTerrain().highlight(TerrainHighlight.Color.Yellow);
			selectedAction.setupAction();
		}
	}
}
