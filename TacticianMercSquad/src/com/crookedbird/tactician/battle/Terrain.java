package com.crookedbird.tactician.battle;

import java.util.List;

import com.crookedbird.engine.GameEngine;
import com.crookedbird.engine.database.GameData;
import com.crookedbird.engine.database.GameDataQuery;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.worldobject.ClickHandler;
import com.crookedbird.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.unit.Unit;

public class Terrain extends WorldObject {
	private Level level;
	private boolean passable = true;
	private TerrainHighlight highlight;
	private Unit unit;

	public Terrain(Level l, String theme, List<String> type, int x, int y) {
		this(l, GameEngine.getEngine().getFromGameDatabase("Terrain",
				new TerrainQuery(theme, type)), x, y);
	}

	public Terrain(Level l, GameData data, int x, int y) {
		super(l, data.getData("img"), x, y);

		this.level = l;

		highlight = new TerrainHighlight(this, TerrainHighlight.Color.Blue);
		highlight.setVisibility(false);

		this.addChild(highlight);

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
		return getX() / 16;
	}

	public int getGridY() {
		return getY() / 16;
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

	public void highlight(boolean on) {
		highlight.setVisibility(on);
	}

	public boolean isHighlight() {
		return highlight.getVisibility();
	}
}
