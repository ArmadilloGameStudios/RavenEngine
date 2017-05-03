package com.crookedbird.tactician.battle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.input.MouseClickInput;
import com.raven.engine.worldobject.ClickHandler;
import com.raven.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.unit.Unit;
import com.crookedbird.tactician.battle.unit.action.UnitAction;

public class Terrain extends WorldObject {
	private Level level;
	private boolean passable = true;
	private Map<String, TerrainHighlight> highlights;
	private Unit unit;
	private UnitAction action;

	public Terrain(Level l, String theme, List<String> type, int x, int y) {
		this(l, GameEngine.getEngine().getRandomFromGameDatabase("Terrain",
				new TerrainQuery(theme, type)), x, y);
	}

	public Terrain(Level l, GameData data, int x, int y) {
		super(l, data.getData("img"), x, y);

		this.level = l;

		highlights = new HashMap<String, TerrainHighlight>();

		highlights.put(TerrainHighlight.Color.Blue, new TerrainHighlight(this,
				TerrainHighlight.Color.Blue));
		highlights.put(TerrainHighlight.Color.Red, new TerrainHighlight(this,
				TerrainHighlight.Color.Red));
		highlights.put(TerrainHighlight.Color.Green, new TerrainHighlight(this,
				TerrainHighlight.Color.Green));
		highlights.put(TerrainHighlight.Color.Yellow, new TerrainHighlight(
				this, TerrainHighlight.Color.Yellow));
		highlights.put(TerrainHighlight.Color.Magenta, new TerrainHighlight(
				this, TerrainHighlight.Color.Magenta));
		highlights.put(TerrainHighlight.Color.Cyan, new TerrainHighlight(this,
				TerrainHighlight.Color.Cyan));

		for (TerrainHighlight th : highlights.values()) {
			th.setVisibility(false);
			this.addChild(th);
		}

		passable = data.getData("Passable") != null
				&& data.getData("Passable").getBoolean();

		addClickHandler(new ClickHandler() {
			@Override
			public void onMouseClick(MouseClickInput e) {
				switch (level.getState()) {
				case UNIT_SELECTION:
					break;
				case ACTION_SELECTION:
					break;
				case ACTION_TARGET_SELECTION:
					level.getSelectedUnit().executeAction(getGridX(),
							getGridY());
					break;
				case EXECUTING_ACTION:
					break;
				default:
					break;
				}
			}
		});
	}

	public int getGridX() {
		return (int)getX() / 16;
	}

	public int getGridY() {
		return (int)getY() / 16;
	}

	public void setUnit(Unit u) {
		this.unit = u;
	}

	public Unit getUnit() {
		return unit;
	}

	public boolean isPassable() {
		return passable;
	}

	public void highlightOff() {
		for (TerrainHighlight th : highlights.values()) {
			th.setVisibility(false);
		}
	}

	public void highlight(String color) {
		highlights.get(color).setVisibility(true);
	}

	public boolean isHighlight() {
		for (TerrainHighlight th : highlights.values()) {
			if (th.getVisibility() == true)
				return true;
		}

		return false;
	}

	public boolean hasAction() {
		return this.action != null;
	}
	
	public void setAction(UnitAction action) {
		this.action = action;
		highlight(action.getColor());
	}
	
	public void removeAction() {
		this.action = null;
		this.highlightOff();
	}
	
	public UnitAction getAction() {
		return action;
	}
}
