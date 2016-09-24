package com.crookedbird.tactician.battle.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.crookedbird.tactician.battle.unit.action.ActionAttack;
import com.crookedbird.tactician.battle.unit.action.ActionMove;
import com.crookedbird.tactician.battle.unit.action.ActionNextUnit;
import com.crookedbird.tactician.battle.unit.action.UnitAction;
import com.crookedbird.tactician.player.Player;

public class Unit extends WorldObject {
	private BattleScene battleScene;
	private Level level;
	private Terrain terrain;
	private int tx, ty;
	private int team;
	private List<UnitAction> unitActions = new ArrayList<UnitAction>();
	private UnitAction selectedAction;
	private UnitStats stats;
	private UnitInteraction unitInteraction;
	private Player player;

	public Unit(BattleScene battleScene, Terrain terrain, final int team) {
		super(battleScene.getLevel(), GameEngine.getEngine()
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
				}).getData("img"), terrain.getX(), terrain.getY());

		this.tx = terrain.getGridX();
		this.ty = terrain.getGridY();
		this.terrain = terrain;
		terrain.setUnit(this);
		this.team = team;
		int stm = range(8, 8);
		this.stats = new UnitStats(range(3, 4), // mov
				range(1, 100), // init
				0, // acc
				0, // dge
				stm, stm, // stm
				stm, // rec
				20, 20, // hps
				0, // res
				range(3, 5) // wgt
		);
		this.battleScene = battleScene;
		this.level = battleScene.getLevel();
		this.unitInteraction = new UnitInteraction(this);

		this.unitActions.add(new ActionMove(this, battleScene));
		this.unitActions.add(new ActionAttack(this, battleScene));
		this.unitActions.add(new ActionNextUnit(this, battleScene));

		this.player = this.battleScene.getPlayer(this.team);
	}

	private int range(int min, int max) {
		return (int) Math.floor(Math.random() * (max - min)) + min;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(int tx, int ty) {
		setX(tx * 16);
		setY(ty * 16);
		this.tx = tx;
		this.ty = ty;
		this.terrain = level.getTerrain()[tx][ty];
	}

	public void setTerrain(Terrain t) {
		setX(t.getGridX() * 16);
		setY(t.getGridY() * 16);
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

	public List<UnitAction> getUnitActions() {
		return this.unitActions;
	}

	public Player getPlayer() {
		return player;
	}

	public void selectUnit() {
		battleScene.setSelectedUnit(this);
	}

	public void selectAction(UnitAction action) {
		if (selectedAction != null)
			selectedAction.cleanAction();

		selectedAction = action;

		battleScene.setState(BattleSceneState.ACTION_TARGET_SELECTION);
	}

	public void executeAction(int x, int y) {
		executeAction(level.getTerrain()[x][y]);
	}

	public void executeAction(Terrain t) {
		if (t.hasAction()) {
			selectedAction.startAction(t);

			interaction().excersise(selectedAction.stmCost());
			battleScene.updateStats();

			battleScene.setState(BattleSceneState.EXECUTING_ACTION);
		}
	}

	public UnitInteraction interaction() {
		return unitInteraction;
	}

	public void kill() {
		this.terrain.setUnit(null);
		battleScene.removeUnit(this);
	}

	@Override
	public void onUpdate(float deltaTime) {
		switch (level.getState()) {
		case UNIT_SELECTION:
			break;
		case ACTION_SELECTION:
			break;
		case ACTION_TARGET_SELECTION:
			break;
		case EXECUTING_ACTION:
			if (selectedAction != null) {
				if (selectedAction.tickAction(deltaTime)) {
					selectedAction.cleanAction();
					if (player.isHuman())
						selectedAction.setupAction();
					else {
						battleScene.setSelectedUnit(battleScene
								.getSelectedUnit());
					}
				}
			}
			break;
		default:
			break;
		}
	}

	public int getDistance(Unit foe) {
		return getDistance(foe.getGridX(), foe.getGridY());
	}

	public int getDistance(int x, int y) {
		List<Pair> path = getPath(x, y);
		if (path == null)
			return 0;

//		for (Pair p : path) {
//			this.level.getTerrain()[p.getX()][p.getY()]
//					.highlight(TerrainHighlight.Color.Green);
//		}
		
		return path.size();
		// return Math.abs(getGridX() - x) + Math.abs(getGridY() - y);
	}

	public List<Pair> getPath(int x, int y) {
		List<Pair> searchedPoints = new ArrayList<Pair>();
		searchedPoints.add(new Pair(getTerrain().getGridX(),
				getTerrain().getGridY()));

		List<List<Pair>> allPaths = new ArrayList<List<Pair>>();
		allPaths.add(new ArrayList<Pair>(searchedPoints));

		return getPath(x, y, searchedPoints, allPaths);
	}

	public List<Pair> getPath(int x, int y,
			List<Pair> searchedPoints,
			List<List<Pair>> allPaths) {

		// find all valid search points
		Map<Pair, List<Pair>> nextSearchPoints = new HashMap<Pair, List<Pair>>();

		for (List<Pair> path : allPaths) {
			Pair endPoint = path.get(path.size() - 1);

			Pair nextPoint = null;
			
			nextPoint = new Pair(endPoint.getX() + 1, endPoint.getY());
			if (!searchedPoints.contains(nextPoint)
					&& !nextSearchPoints.keySet().contains(nextPoint)) {

				searchedPoints.add(nextPoint);

				List<Pair> newPath = new ArrayList<Pair>(path);
				newPath.add(nextPoint);

				nextSearchPoints.put(nextPoint, newPath);
				//System.out.println(newPath);
				
				if (nextPoint.getX() == x && nextPoint.getY() == y) {
					return nextSearchPoints.get(nextPoint);
				}
			}

			nextPoint = new Pair(endPoint.getX() - 1, endPoint.getY());
			if (!searchedPoints.contains(nextPoint)
					&& !nextSearchPoints.keySet().contains(nextPoint)) {

				searchedPoints.add(nextPoint);

				List<Pair> newPath = new ArrayList<Pair>(path);
				newPath.add(nextPoint);

				nextSearchPoints.put(nextPoint, newPath);
				//System.out.println(newPath);

				if (nextPoint.getX() == x && nextPoint.getY() == y) {
					return nextSearchPoints.get(nextPoint);
				}
			}

			nextPoint = new Pair(endPoint.getX(), endPoint.getY() + 1);
			if (!searchedPoints.contains(nextPoint)
					&& !nextSearchPoints.keySet().contains(nextPoint)) {

				searchedPoints.add(nextPoint);

				List<Pair> newPath = new ArrayList<Pair>(path);
				newPath.add(nextPoint);

				nextSearchPoints.put(nextPoint, newPath);
				//System.out.println(newPath);

				if (nextPoint.getX() == x && nextPoint.getY() == y) {
					return nextSearchPoints.get(nextPoint);
				}
			}

			nextPoint = new Pair(endPoint.getX(), endPoint.getY() - 1);
			if (!searchedPoints.contains(nextPoint)
					&& !nextSearchPoints.keySet().contains(nextPoint)) {

				searchedPoints.add(nextPoint);

				List<Pair> newPath = new ArrayList<Pair>(path);
				newPath.add(nextPoint);

				nextSearchPoints.put(nextPoint, newPath);
				//System.out.println(newPath);

				if (nextPoint.getX() == x && nextPoint.getY() == y) {
					return nextSearchPoints.get(nextPoint);
				}
			}
		}

		// check points & create new allPaths
		checkPoints(allPaths, nextSearchPoints);

		if (nextSearchPoints.keySet().size() > 0) {
			return getPath(x, y, searchedPoints, allPaths);
		} else {
			return null;
		}
	}

	private void checkPoints(List<List<Pair>> allPaths,
			Map<Pair, List<Pair>> nextSearchPoints) {
		allPaths.clear();

		for (Pair point : nextSearchPoints.keySet()) {
			if (pathfindIsWalkable(point.getX(), point.getY())) {
				allPaths.add(nextSearchPoints.get(point));
			}
		}
	}

	private boolean pathfindIsWalkable(int x, int y) {
		Terrain t = null;
		

		if (battleScene.getLevel().getTerrain().length > x && x > 0
				&& battleScene.getLevel().getTerrain()[x].length > y && y > 0) {
			t = battleScene.getLevel().getTerrain()[x][y];
		}

		return t != null && t.isPassable()
				&& (t.getUnit() == null || t.getUnit().getTeam() == getTeam());
	}

	public Terrain closestTerrain(List<Terrain> terrain) {
		Terrain ct = null;
		int dist = 0;

		for (Terrain t : terrain) {
			int d = getDistance(t.getGridX(), t.getGridY());
			if ((d < dist && d != 0) || dist == 0) {
				dist = d;
				ct = t;
			}
		}

		return ct;
	}
}
