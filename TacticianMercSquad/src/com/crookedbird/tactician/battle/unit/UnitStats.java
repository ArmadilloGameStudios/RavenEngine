package com.crookedbird.tactician.battle.unit;

public class UnitStats {
	private int movement = 0;     // mov
	private int initiative = 0;   // init
	
	private int accuracy = 0;     // acc
	private int dodge = 0;        // dge
	
	private int maxStamina = 0;   // stm
	private int currentStamina = 0;
	private int recovery = 0;     // rec
	
	private int maxHitPoints = 0; // hp
	private int currentHitPoints = 0;
	
	private int resistance = 0;   // res
	private int absorption = 0;   // abs

	
	public UnitStats(
			int movement, int initiative,
			int accuracy, int dodge, 
			int maxStamina, int currentStamina, int recovery, 
			int maxHitPoints, int currentHitPoints, 
			int resistance, int absorption) {
		
		this.movement = movement;
		this.initiative = initiative;
		this.accuracy = accuracy;
		this.dodge = dodge;
		this.maxStamina = maxStamina;
		this.currentStamina = currentStamina;
		this.recovery = recovery;
		this.maxHitPoints = maxHitPoints;
		this.currentHitPoints = currentHitPoints;
		this.resistance = resistance;
		this.absorption = absorption;
	}

	public int getMovement() {
		return movement;
	}
	public void setMovement(int movement) {
		this.movement = movement;
	}
	public int getInitiative() {
		return initiative;
	}
	public void setInitiative(int initiative) {
		this.initiative = initiative;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	public int getDodge() {
		return dodge;
	}
	public void setDodge(int dodge) {
		this.dodge = dodge;
	}
	public int getMaxStamina() {
		return maxStamina;
	}
	public void setMaxStamina(int maxStamina) {
		this.maxStamina = maxStamina;
	}
	public int getRecovery() {
		return recovery;
	}
	public void setRecovery(int recovery) {
		this.recovery = recovery;
	}
	public int getMaxHitPoints() {
		return maxHitPoints;
	}
	public void setMaxHitPoints(int maxHitPoints) {
		this.maxHitPoints = maxHitPoints;
	}
	public int getResistance() {
		return resistance;
	}
	public void setResistance(int resistance) {
		this.resistance = resistance;
	}
	public int getAbsorption() {
		return absorption;
	}
	public void setAbsorption(int absorption) {
		this.absorption = absorption;
	}
	public int getCurrentStamina() {
		return currentStamina;
	}
	public void setCurrentStamina(int currentStamina) {
		this.currentStamina = currentStamina;
	}
	public int getCurrentHitPoints() {
		return currentHitPoints;
	}
	public void setCurrentHitPoints(int currentHitPoints) {
		this.currentHitPoints = currentHitPoints;
	}
}
