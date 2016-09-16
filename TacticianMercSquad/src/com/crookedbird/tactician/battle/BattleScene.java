package com.crookedbird.tactician.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.crookedbird.engine.Game;
import com.crookedbird.engine.scene.Layer;
import com.crookedbird.engine.scene.Scene;
import com.crookedbird.tactician.battle.sidebar.ActionSidebar;
import com.crookedbird.tactician.battle.sidebar.StatSidebar;
import com.crookedbird.tactician.battle.unit.Unit;
import com.crookedbird.tactician.battle.unit.action.UnitAction;
import com.crookedbird.tactician.generation.Generation;
import com.crookedbird.tactician.generation.LevelGenerationProperties;

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
		
		actionSideBar = new ActionSidebar(playerSideBarLayer, level.getWidth() + level.getGlobalX(), 0);
		playerSideBarLayer.addChild(actionSideBar);

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
			level.addChild(u.getWorldObject());
		}

		Collections.sort(units, new Comparator<Unit>() {
			@Override
			public int compare(Unit a, Unit b) {
				int i = b.getStats().getInitiative()
						- a.getStats().getInitiative();

				if (i == 0) {
					i = a.getStats().getMaxStamina() - b.getStats().getMaxStamina();
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
		System.out.println(state);
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

		statSideBar.updateStats(unit.getStats());
	}

	public void setSelectedUnit(Unit unit) {
		selectedUnit = unit;
		selectedUnitIndex = units.indexOf(unit);

		statSideBar.updateStats(unit.getStats());
	}

	public Unit getNextSelectedUnit() {
		selectedUnitIndex++;

		if (selectedUnitIndex == units.size()) {
			selectedUnitIndex = 0;
		}

		return units.get(selectedUnitIndex);
	}

	Unit getSelectedUnit() {
		return selectedUnit;
	}

	public void setUnitAction(List<UnitAction> unitActions) {
		actionSideBar.setUnitActions(unitActions);
	}
}
