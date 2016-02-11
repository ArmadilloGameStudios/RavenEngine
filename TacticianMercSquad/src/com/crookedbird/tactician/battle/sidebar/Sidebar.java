package com.crookedbird.tactician.battle.sidebar;

import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.worldobject.Parentable;
import com.crookedbird.engine.worldobject.WorldObject;
import com.crookedbird.tactician.battle.unit.UnitStats;

public class Sidebar extends WorldObject {
	static class Stats {
		static final int Movement = 0;
		static final int Initiative = 1;
		static final int Accuracy = 2;
		static final int Dodge = 3;
		static final int Stamina = 4;
		static final int HitPoints = 5;
		static final int Resistance = 6;
		static final int Absorption = 7;
	}

	private List<Holder> holders = new ArrayList<Holder>();

	public Sidebar(Parentable parent) {
		super(parent, null);

		for (int i = 0; i < 8; i++) {
			Holder h = new Holder(this, 0, 64 * i);
			holders.add(h);
			this.addChild(h);

			switch (i) {
			case Stats.Movement:
				h.setDescription("Movement");
				h.setValue("0");
				break;
			case Stats.Initiative:
				h.setDescription("Initiative");
				h.setValue("0");
				break;
			case Stats.Accuracy:
				h.setDescription("Accuracy");
				h.setValue("0");
				break;
			case Stats.Dodge:
				h.setDescription("Dodge");
				h.setValue("0");
				break;
			case Stats.Stamina:
				h.setDescription("Stamina");
				h.setValue("0");
				break;
			case Stats.HitPoints:
				h.setDescription("Hit Points");
				h.setValue("0");
				break;
			case Stats.Resistance:
				h.setDescription("Resistance");
				h.setValue("0");
				break;
			case Stats.Absorption:
				h.setDescription("Absorption");
				h.setValue("0");
				break;
			}
		}
	}

	public void updateStats(UnitStats stats) {
		holders.get(Stats.Movement).setValue(String.valueOf(stats.getMovement()));
		holders.get(Stats.Initiative).setValue(String.valueOf(stats.getInitiative()));
		holders.get(Stats.Accuracy).setValue(String.valueOf(stats.getAccuracy()));
		holders.get(Stats.Dodge).setValue(String.valueOf(stats.getDodge()));
		holders.get(Stats.Stamina).setValue(stats.getCurrentStamina() + "/" + stats.getMaxStamina());
		holders.get(Stats.HitPoints).setValue(stats.getCurrentHitPoints() + "/" + stats.getMaxHitPoints());
		holders.get(Stats.Resistance).setValue(String.valueOf(stats.getResistance()));
		holders.get(Stats.Absorption).setValue(String.valueOf(stats.getAbsorption()));
	}
}
