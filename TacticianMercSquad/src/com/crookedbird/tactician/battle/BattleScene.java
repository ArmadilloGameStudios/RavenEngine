package com.crookedbird.tactician.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raven.engine.Game;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.crookedbird.tactician.battle.sidebar.ActionSidebar;
import com.crookedbird.tactician.battle.sidebar.StatSidebar;
import com.crookedbird.tactician.battle.unit.Unit;
import com.crookedbird.tactician.battle.unit.action.UnitAction;
import com.crookedbird.tactician.generation.Generation;
import com.crookedbird.tactician.generation.LevelGenerationProperties;
import com.crookedbird.tactician.player.Player;
import com.crookedbird.tactician.player.PlayerAI;

public class BattleScene extends Scene {
	private Game game;
	private Level level;
	private Layer levelLayer, playerSideBarLayer;
	private int levelOffset = 192;
	private List<Unit> units = new ArrayList<Unit>();
	private Unit selectedUnit;
	private int selectedUnitIndex = 0;
	private BattleSceneState state = BattleSceneState.UNIT_SELECTION;
	private StatSidebar statSideBar;
	private ActionSidebar actionSideBar;
	private Map<Integer, Player> players = new HashMap<Integer, Player>();
	private Player activePlayer;

	public BattleScene(Game game) {
		super(game);
		this.game = game;
	}

	@Override
	public void enterScene() {
		addLayer(levelLayer = new Layer(this));
		addLayer(playerSideBarLayer = new Layer(this));

		// Level
		level = Generation.LevelGeneration(new LevelGenerationProperties(),
				this);
		levelLayer.addChild(level);
		level.setX(levelOffset);

		// Sidebar
		statSideBar = new StatSidebar(playerSideBarLayer);
		playerSideBarLayer.addChild(statSideBar);

		actionSideBar = new ActionSidebar(playerSideBarLayer, this,
				level.getWidth() + (int) level.getGlobalX(), 0);
		playerSideBarLayer.addChild(actionSideBar);

		// players
		players.put(0, new PlayerAI(this, 0));
		players.put(1, new PlayerAI(this, 1));

		// TODO
		// remove, just for test
		int l = level.getTerrain().length;
		units.add(new Unit(this, level.getTerrain()[l / 2][0], 0));
		units.add(new Unit(this, level.getTerrain()[(l / 2) + 1][0], 0));
		units.add(new Unit(this, level.getTerrain()[(l / 2) - 1][0], 0));
		units.add(new Unit(this, level.getTerrain()[l / 2][1], 0));
		units.add(new Unit(this, level.getTerrain()[(l / 2) + 1][1], 0));
		units.add(new Unit(this, level.getTerrain()[(l / 2) - 1][1], 0));

		int h = level.getTerrain()[0].length - 1;
		units.add(new Unit(this, level.getTerrain()[l / 2][h], 1));
		units.add(new Unit(this, level.getTerrain()[(l / 2) + 1][h], 1));
		units.add(new Unit(this, level.getTerrain()[(l / 2) - 1][h], 1));
		units.add(new Unit(this, level.getTerrain()[l / 2][h - 1], 1));
		units.add(new Unit(this, level.getTerrain()[(l / 2) + 1][h - 1], 1));
		units.add(new Unit(this, level.getTerrain()[(l / 2) - 1][h - 1], 1));

		for (Unit u : units) {
			level.addChild(u);
		}

		Collections.sort(units, new Comparator<Unit>() {
			@Override
			public int compare(Unit a, Unit b) {
				int i = b.getStats().getInitiative()
						- a.getStats().getInitiative();

				if (i == 0) {
					i = a.getStats().getMaxStamina()
							- b.getStats().getMaxStamina();
				}
				if (i == 0) {
					i = a.getStats().getMovement() - b.getStats().getMovement();
				}

				return i;
			}
		});

		units.get(0).selectUnit();
	}

	@Override
	public void exitScene() {
		// TODO Auto-generated method stub

	}

	public BattleSceneState getState() {
		return state;
	}

	public void setState(BattleSceneState state) {
		this.state = state;
	}

	protected Layer getLevelLayer() {
		return levelLayer;
	}

	public Level getLevel() {
		return level;
	}

	public void setSelectedUnit(Unit unit, int index) {
		selectedUnit = unit;
		selectedUnitIndex = 0;

		selectedUnit();
	}

	public void setSelectedUnit(Unit unit) {
		selectedUnit = unit;
		selectedUnitIndex = units.indexOf(unit);

		selectedUnit();
	}

	private void selectedUnit() {

		if (selectedUnit.getPlayer().isHuman()) {
			updateStats();

			setState(BattleSceneState.ACTION_SELECTION);

			setUnitAction(selectedUnit.getUnitActions());
			selectedUnit.getUnitActions().get(0).setupAction();

			selectedUnit.getTerrain().highlight(TerrainHighlight.Color.Yellow);
		} else {
			for (int i = 0; i < level.getTerrain().length; i++) {
				for (int j = 0; j < level.getTerrain()[i].length; j++) {
					level.getTerrain()[i][j].highlightOff();
				}
			}
			
			Player ai = selectedUnit.getPlayer();
			UnitAction action = ai.selectAction(selectedUnit.getUnitActions());

			selectedUnit.selectAction(action);

			setState(BattleSceneState.EXECUTING_ACTION);
			action.startAction(ai.selectTerrain(action));

			selectedUnit.interaction().excersise(action.stmCost());
			
		}
	}

	public Unit getNextSelectedUnit() {
		int index = selectedUnitIndex + 1;

		if (index >= units.size()) {
			index = 0;
		}

		return units.get(index);
	}

	public Unit getSelectedUnit() {
		return selectedUnit;
	}

	public List<Unit> getUnits() {
		return this.units;
	}

	public List<Unit> getTeamUnits(int team) {
		List<Unit> ut = new ArrayList<Unit>();

		for (Unit u : units) {
			if (u.getTeam() == team)
				ut.add(u);
		}

		return ut;
	}

	public List<Unit> getOtherTeamUnits(int team) {
		List<Unit> ut = new ArrayList<Unit>();

		for (Unit u : units) {
			if (u.getTeam() != team)
				ut.add(u);
		}

		return ut;
	}

	public void setUnitAction(List<UnitAction> unitActions) {
		actionSideBar.setUnitActions(unitActions);
	}

	public void updateStats() {
		if (selectedUnit != null) {
			statSideBar.updateStats(selectedUnit.getStats());
		}
	}

	public void removeUnit(Unit unit) {
		this.units.remove(unit);

		this.level.removeChild(unit);
	}

	public Player getPlayer(int team) {
		return players.get(team);
	}
}
