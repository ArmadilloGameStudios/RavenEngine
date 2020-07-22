package com.armadillogamestudios.storyteller.resource.trait;

import com.armadillogamestudios.storyteller.gameengine.StoryTeller;

public class Trait {

    private TraitDescription traitDescription;
    private int value;

    private Trait(TraitDescription traitDescription, int value) {
        this.traitDescription = traitDescription;
        this.value = value;
    }

    private Trait(TraitDescription traitDescription) {
        this.traitDescription = traitDescription;
        this.value = traitDescription.getStartingValue();
    }

    public static Trait get(String key) {
        Trait trait;

        if (!StoryTeller.getTraitManager().containsKey(key)) {
            throw new IllegalArgumentException(key + " not created");
        }

        trait = new Trait(StoryTeller.getTraitManager().get(key));

        return trait;
    }

    public static boolean exist(String key) {
        return StoryTeller.getTraitManager().containsKey(key);
    }

    public void increment(int value) {
        this.value += value;

        if (this.value > traitDescription.getMaxValue()) {
            this.value = traitDescription.getMaxValue();
        }
    }

    public void increment(int value, int max) {
        if (this.value >= max) return;

        this.value += value;

        if (this.value > max) {
            this.value = max;
        }

        if (this.value > traitDescription.getMaxValue()) {
            this.value = traitDescription.getMaxValue();
        }
    }

    public void decrement(int value) {
        this.value -= value;

        if (this.value < 0) {
            this.value = 0;
        }
    }

    public void decrement(int value, int min) {
        if (this.value <= min) return;

        this.value -= value;

        if (this.value < min) {
            this.value = min;
        }

        if (this.value < 0) {
            this.value = 0;
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getProp(String prop) {
        return traitDescription.getProperty(value, prop);
    }

    public TraitDescription getTraitDescription() {
        return traitDescription;
    }

    public String getName() {
        return traitDescription.getName();
    }

    @Override
    public String toString() {
        return "" + traitDescription.toString() + ": " + value;
    }

    public String toDefaultString() {
        return traitDescription.getProperty(value);
    }
}