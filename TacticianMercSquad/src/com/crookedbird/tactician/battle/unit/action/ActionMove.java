package com.crookedbird.tactician.battle.unit.action;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.util.Pair;
import com.crookedbird.tactician.battle.Terrain;
import com.crookedbird.tactician.battle.TerrainHighlight;
import com.crookedbird.tactician.battle.unit.Unit;

public class ActionMove extends UnitAction {
	private List<Terrain> actionTerrain = new ArrayList<Terrain>();
	
	public ActionMove(Unit unit) {
		super(unit);
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


		if (getUnit().getStats().getCurrentStamina() > 0) {
			actionTerrain = pathfind(getUnit().getStats().getMovement());

			for (Terrain t : actionTerrain) {
				if (t != null) {
					t.setAction(this);
				}
			}
		}
	}
	
	public void doAction(Terrain terrain) {
		getUnit().getTerrain().setUnit(null);
		getUnit().setTerrain(terrain);
		getUnit().getTerrain().setUnit(getUnit());

		getUnit().getStats().setCurrentStamina(
				getUnit().getStats().getCurrentStamina()
						- getUnit().getStats().getWeight());
	}
	
	public void cleanAction() {
		for (Terrain t : actionTerrain) {
			t.removeAction();
		}
		actionTerrain.clear();
	}

	public Unit getNextUnit() {
		if (getUnit().getStats().getCurrentStamina() <= 0) {
			Unit u = getUnit().getNextUnit();
			u.recover();
			return u;
		} else {
			return getUnit();
		}
	}
	
	private boolean pathfindIsWalkable(Terrain t, List<Terrain> lst) {
		return t != null && t.isPassable() && t != getUnit().getTerrain()
				&& !lst.contains(t)
				&& (t.getUnit() == null || t.getUnit().getTeam() == getUnit().getTeam());
	}

	private boolean pathfindIsFree(Terrain t, List<Terrain> lst) {
		return t != null && t.isPassable() && t != getUnit().getTerrain()
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
				t = getUnit().getRelitiveTerrain(p.getX(), p.getY() + 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX(), p.getY() + 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX(), p.getY() - 1);
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX(), p.getY() - 1));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX() + 1, p.getY());
				if (pathfindIsWalkable(t, terrain)) {
					nextPoints.add(new Pair<Integer>(p.getX() + 1, p.getY()));

					if (pathfindIsFree(t, terrain)) {
						terrain.add(t);
					}
				}

				t = getUnit().getRelitiveTerrain(p.getX() - 1, p.getY());
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
}
