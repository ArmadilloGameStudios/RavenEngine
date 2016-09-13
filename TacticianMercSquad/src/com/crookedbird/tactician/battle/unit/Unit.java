package com.crookedbird.tactician.battle.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

public class Unit {
	private WorldObject unitWorldObject;
	private BattleScene battleScene;
	private Level level;
	private Terrain terrain;
	private int tx, ty;
	private int team;
	private UnitAction[] unitActions;
	private UnitAction selectedAction;
	private UnitStats stats;

	private List<Terrain> actionTerrain;

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
		this.stats = new UnitStats((int) Math.floor(Math.random() * 5) + 2, 5,
				10, 3, 4, 4, 2, 20, 20, 0, 0);
		this.battleScene = battleScene;
		this.level = battleScene.getLevel();
		this.unitActions = new UnitAction[] { new UnitAction(this) };
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

	private boolean pathfindIsWalkable(Terrain t, List<Terrain> lst) {
		return t != null && t.isPassable() && t != getTerrain()
				&& !lst.contains(t)
				&& (t.getUnit() == null || t.getUnit().getTeam() == team);
	}

	private boolean pathfindIsFree(Terrain t, List<Terrain> lst) {
		return t != null && t.isPassable() && t != getTerrain()
				&& !lst.contains(t) && t.getUnit() == null;
	}

	private List<Terrain> pathfind(int dis) {
		ArrayList<Terrain> terrain = new ArrayList<Terrain>();

		ArrayList<Pair<Integer>> activePoints = new ArrayList<Pair<Integer>>();
		activePoints.add(new Pair<Integer>(0, 0));

		ArrayList<Pair<Integer>> nextPoints = new ArrayList<Pair<Integer>>();

		Terrain t = null;

		for (int i = 0; i < dis; i++) {
			for (Pair<Integer> p : activePoints) {
				t = getRelitiveTerrain(p.getX(), p.getY() + 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX(), p.getY() + 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getRelitiveTerrain(p.getX(), p.getY() - 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX(), p.getY() - 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getRelitiveTerrain(p.getX() + 1, p.getY());
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX() + 1, p.getY()));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getRelitiveTerrain(p.getX() - 1, p.getY());
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX() - 1, p.getY()));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}
			}

			activePoints.clear();
			activePoints.addAll(nextPoints);
			nextPoints.clear();
		}

		return terrain;
	}

	public void selectUnit() {
		battleScene.setSelectedUnit(this);
		battleScene.setState(BattleSceneState.ACTION_SELECTION);

		// TODO
		selectAction();
	}

	public void selectAction() {
		selectedAction = unitActions[0];
		battleScene.setState(BattleSceneState.ACTION_TARGET_SELECTION);

		actionTerrain = pathfind(stats.getMovement());

		for (Terrain t : actionTerrain) {
			if (t != null)
				t.highlight(TerrainHighlight.Color.Blue);
		}
		
		this.getTerrain().highlight(TerrainHighlight.Color.Yellow);
	}

	public void executeAction(int x, int y) {
		battleScene.setState(BattleSceneState.EXECUTING_ACTION);
		
		this.getTerrain().highlightOff();

		if (level.getTerrain()[x][y].isHighlight())
			selectedAction.doAction(x, y);

		for (Terrain t : actionTerrain) {
			if (t != null)
				t.highlightOff();
		}

		actionTerrain = null;
	}
}
