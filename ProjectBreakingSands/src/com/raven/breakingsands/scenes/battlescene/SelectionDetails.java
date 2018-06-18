package com.raven.breakingsands.scenes.battlescene;

public class SelectionDetails {
    public String name;
    public String hp;
    public String movement;
    public String resistance;
    public String weapon;
    public String damage;
    public String piercing;
    public String range;
    public String shots;

    public void clear() {
        name = "-";
        hp = "-";
        movement = "-";
        resistance = "-";
        weapon = "-";
        damage = "-";
        piercing = "-";
        range = "-";
        shots = "-";
    }
}
