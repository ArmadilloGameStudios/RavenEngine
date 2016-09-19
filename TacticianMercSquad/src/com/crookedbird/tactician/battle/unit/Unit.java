package com.crookedbird.tactician.battle.unit;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.worldobject.ClickHandler;
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
		this.stats = new UnitStats(range(3, 10), // mov
				range(4, 40), // init
				0, // acc
				0, // dge
				stm, stm, // stm
				range(6, 6), // rec
				20, 20, // hps
				0, // res
				range(4, 4) // wgt
		);
		this.battleScene = battleScene;
		this.level = battleScene.getLevel();
		this.unitInteraction = new UnitInteraction(this);

		this.unitActions.add(new ActionMove(this, battleScene));
		this.unitActions.add(new ActionAttack(this, battleScene));
		this.unitActions.add(new ActionNextUnit(this, battleScene));

		addClickHandler(new ClickHandler() {
			@Override
			public void onMouseClick(MouseClickInput e) {
				onClick();
			}
		});
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

	public void selectUnit() {
		battleScene.setSelectedUnit(this);
		battleScene.setState(BattleSceneState.ACTION_SELECTION);
		battleScene.setUnitAction(this.unitActions);

		getTerrain().highlight(TerrainHighlight.Color.Yellow);

		this.unitActions.get(0).setupAction();
	}

	public void selectAction(UnitAction action) {
		if (selectedAction != null)
			selectedAction.cleanAction();

		selectedAction = action;

		battleScene.setState(BattleSceneState.ACTION_TARGET_SELECTION);
	}

	private int eax, eay;

	public void executeAction(int x, int y) {
		if (level.getTerrain()[x][y].hasAction()) {			
			selectedAction.startAction(level.getTerrain()[x][y]);

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

	public void onClick() {
		switch (level.getState()) {
		case UNIT_SELECTION:
			selectUnit();
			break;
		case ACTION_SELECTION:
			break;
		case ACTION_TARGET_SELECTION:
			break;
		case EXECUTING_ACTION:
			break;
		default:
			break;
		}
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
					selectedAction.setupAction();
				}
			}
			break;
		default:
			break;
		}
	}
}
