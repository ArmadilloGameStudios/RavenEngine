package com.crookedbird.tactician.battle.unit;

public class UnitInteraction {
	private Unit unit;

	public UnitInteraction(Unit unit) {
		this.unit = unit;
	}

	public boolean damage(int dmg) {
		unit.getStats().setCurrentHitPoints(
				unit.getStats().getCurrentHitPoints() - dmg);

		if (unit.getStats().getCurrentHitPoints() <= 0) {
			return true;
		}
		
		return false;
	}

	public void excersise() {
		unit.getStats().setCurrentStamina(
				unit.getStats().getCurrentStamina()
						- unit.getStats().getWeight());
	}

	public void excersise(int wgt) {
		unit.getStats().setCurrentStamina(
				unit.getStats().getCurrentStamina() - wgt);
	}

	public void recover() {
		int cs = unit.getStats().getRecovery() + unit.getStats().getCurrentStamina();

		if (cs > unit.getStats().getMaxStamina()) {
			cs = unit.getStats().getMaxStamina();
		}

		unit.getStats().setCurrentStamina(cs);
	}
}
