package com.crookedbird.tactician.battle.sidebar;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.worldobject.ClickHandler;
import com.crookedbird.engine.worldobject.Parentable;
import com.crookedbird.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.BattleScene;
import com.crookedbird.tactician.battle.unit.action.UnitAction;

public class ActionSidebar extends WorldObject {
	private BattleScene battleScene;

	private List<Holder> holders = new ArrayList<Holder>();

	public ActionSidebar(Parentable parent, BattleScene bs, int x, int y) {
		super(parent, null, x, y);
		battleScene = bs;
	}

	public void clear() {
		holders.clear();
		removeAllChildren();
	}

	public void setUnitActions(List<UnitAction> unitActions) {
		clear();

		for (final UnitAction action : unitActions) {
			Holder h = new Holder(this, 0, 64 * holders.size());
			holders.add(h);
			addChild(h);

			h.setDescription(action.getName());

			h.addClickHandler(new ClickHandler() {
				@Override
				public void onMouseClick(MouseClickInput m) {

					switch (battleScene.getState()) {
					case UNIT_SELECTION:
						break;
					case ACTION_SELECTION:
					case ACTION_TARGET_SELECTION:
						action.setupAction();
						break;
					case EXECUTING_ACTION:
						break;
					default:
						break;
					}
				}
			});
		}

		this.setHeight(64 * holders.size());
		this.setWidth(192);
	}
}