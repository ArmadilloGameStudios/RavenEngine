package com.armadillogamestudios.storyteller.scenario.prompt.input;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Targetsable;

import java.util.ArrayList;
import java.util.List;

public class InputEvent {

    private final Targetsable input;
    private final List<InputEventTrait> addTraits = new ArrayList<>();
    private final List<InputEventTrait> removeTraits = new ArrayList<>();
    private final List<InputEventTrait> incrementTraits = new ArrayList<>();
    private final List<InputEventTrait> decrementTraits = new ArrayList<>();

    private int targetIndex;

    public InputEvent(GameData gameData, Targetsable input) {

        this.input = input;

        gameData.ifHas("target",
                i -> this.targetIndex = i.asInteger(),
                () -> this.targetIndex = 0);

        gameData.ifHas("add_traits",
                ts -> ts.asList().forEach(t -> {
                    addTraits.add(new InputEventTrait(t));
                }));

        gameData.ifHas("add_trait",
                t -> addTraits.add(new InputEventTrait(t)));

        gameData.ifHas("increment_traits",
                ts -> ts.asList().forEach(t -> {
                    incrementTraits.add(new InputEventTrait(t));
                }));

        gameData.ifHas("increment_trait",
                t -> incrementTraits.add(new InputEventTrait(t)));

        gameData.ifHas("decrement_traits",
                ts -> ts.asList().forEach(t -> {
                    decrementTraits.add(new InputEventTrait(t));
                }));

        gameData.ifHas("decrement_trait",
                t -> decrementTraits.add(new InputEventTrait(t)));

        gameData.ifHas("remove_traits",
                ts -> ts.asList().forEach(t -> {
                    removeTraits.add(new InputEventTrait(t));
                }));

        gameData.ifHas("remove_trait",
                t -> removeTraits.add(new InputEventTrait(t)));
    }

    public void trigger() {
        Resource target = input.getTargets()[targetIndex];

        addTraits.forEach(trait -> target.addTrait(trait.key, trait.value));
        incrementTraits.forEach(trait -> target.incrementTrait(trait.key, trait.value, trait.max));
        decrementTraits.forEach(trait -> target.decrementTrait(trait.key, trait.value, trait.min));
        removeTraits.forEach(trait -> target.removeTrait(trait.key));
    }

    private static class InputEventTrait {
        public final String key;
        public int min = -1, max = -1, value;

        public InputEventTrait(GameData gameData) {
            this.key = gameData.getString("key");

            gameData.ifHas("value", gd -> this.value = gd.asInteger());
            gameData.ifHas("min", gd -> this.min = gd.asInteger());
            gameData.ifHas("max", gd -> this.max = gd.asInteger());
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
