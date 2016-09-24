package com.crookedbird.tactician.battle.unit.action;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.util.Pair;
import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.Unit;

public class ActionMove extends UnitAction {
	private List<Terrain> actionTerrain = new ArrayList<Terrain>();
	private Terrain moveToTerrain;
	private Terrain moveFromTerrain;
	private float tickTotal = 0.0F;
	private int totalLength = 0;

	public ActionMove(Unit unit, BattleScene bs) {
		super(unit, bs);
		moveFromTerrain = getUnit().getTerrain();
	}

	@Override
	public String getColor() {
		return TerrainHighlight.Color.Blue;
	}

	public String getName() {
		return "Move";
	}

	@Override
	public void setupAction() {
		getUnit().selectAction(this);

		getUnit().getTerrain().highlight(TerrainHighlight.Color.Yellow);

		if (getUnit().getStats().getCurrentStamina() >= stmCost()) {
			actionTerrain = pathfind(getUnit().getStats().getMovement());

			for (Terrain t : actionTerrain) {
				if (t != null) {
					t.setAction(this);
				}
			}
		}
	}

	@Override
	public int stmCost() {
		return getUnit().getStats().getWeight();
	}

	public void startAction(Terrain terrain) {
		tickTotal = 0.0F;

		moveToTerrain = terrain;
		moveFromTerrain = getUnit().getTerrain();

		totalLength = getUnit().setAnimationState("teleport");
	}

	@Override
	public boolean tickAction(float deltaTime) {
		if (tickTotal < totalLength / 2F
				&& tickTotal + deltaTime > totalLength / 2F) {
			getUnit().getTerrain().setUnit(null);
			getUnit().setTerrain(moveToTerrain);
			getUnit().getTerrain().setUnit(getUnit());
		} else if (tickTotal + deltaTime > totalLength) {
			return true;
		}

		tickTotal += deltaTime;

		return false;
	}

	public void cleanAction() {
		getUnit().setAnimationState("idle");
		moveFromTerrain.highlightOff();

		for (Terrain t : actionTerrain) {
			t.removeAction();
		}
		actionTerrain.clear();
	}

	public List<Terrain> getTerrain() {
		return pathfind(getUnit().getStats().getMovement());
	}

	private boolean pathfindIsWalkable(Terrain t, List<Terrain> lst) {
		return t != null
				&& t.isPassable()
				&& t != getUnit().getTerrain()
				&& !lst.contains(t)
				&& (t.getUnit() == null || t.getUnit().getTeam() == getUnit()
						.getTeam());
	}

	private boolean pathfindIsFree(Terrain t, List<Terrain> lst) {
		return t != null && t.isPassable() && t != getUnit().getTerrain()
				&& !lst.contains(t) && t.getUnit() == null;
	}

	private List<Terrain> pathfind(int dis) {
		ArrayList<Terrain> terrain = new ArrayList<Terrain>();

		ArrayList<Pair> activePoints = new ArrayList<Pair>();
		activePoints.add(new Pair(0, 0));

		ArrayList<Pair> nextPoints = new ArrayList<Pair>();

		Terrain t = null;

		for (int i = 0; i < dis; i++) {
			for (Pair p : activePoints) {
				t = getUnit().getRelitiveTerrain(p.getX(), p.getY() + 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair(p.getX(), p.getY() + 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX(), p.getY() - 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair(p.getX(), p.getY() - 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX() + 1, p.getY());
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair(p.getX() + 1, p.getY()));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX() - 1, p.getY());
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair(p.getX() - 1, p.getY()));

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

	@Override
	public ActionType getType() {
		return ActionType.MOVE;
	}
}
