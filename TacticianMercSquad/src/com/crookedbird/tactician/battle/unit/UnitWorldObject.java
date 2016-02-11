package com.crookedbird.tactician.battle.unit;

import com.crookedbird.engine.database.GameDataRow;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.worldobject.ClickHandler;
import com.crookedbird.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.Level;

public class UnitWorldObject extends WorldObject {
	private Level level;
	private Unit unit;

	public UnitWorldObject(final Unit unit, final Level level,
			GameDataRow anim, int x, int y, int w, int h) {
		super(level, anim, x, y, w, h);

		this.unit = unit;
		this.level = level;

		addClickHandler(new ClickHandler() {
			@Override
			public void onMouseClick(MouseClickInput e) {
				switch (level.getState()) {
				case UNIT_SELECTION:
					unit.selectUnit();
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
		});
	}
}
