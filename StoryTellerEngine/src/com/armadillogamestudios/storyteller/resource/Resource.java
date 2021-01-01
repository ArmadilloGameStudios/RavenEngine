package com.armadillogamestudios.storyteller.resource;

import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.layout.Layout;
import com.armadillogamestudios.storyteller.resource.trait.Trait;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Resource {

    private String name, layout;
    private Map<String, Trait> traitMap = new HashMap<>();
    private Set<Resource> resources = new HashSet<>();

    public Resource(String name, String layout) {
        this.name = name;
        this.layout = layout;

        applyLayout(layout);
    }

    private void applyLayout(String layoutName) {
        Layout layout = StoryTeller.getLayoutManager().get(layoutName);

        if (layout == null) return;

        if (layout.hasInherits()) {
            applyLayout(layout.getInherits());
        }

        if (layout.hasTraits()) {
            layout.getTraits().forEach((trait) -> {
                this.addTrait(trait.key, trait.value);
            });
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    protected final void removeResource(Resource child) {
        resources.remove(child);
    }

    protected final void addResource(Resource child) {
        resources.add(child);
    }

    public boolean hasResource() {
        return !resources.isEmpty();
    }

    public boolean hasResource(String trait) {
        return resources.stream().anyMatch(r -> r.hasTrait(trait));
    }

    public Resource getResource() {
        return resources.stream().findFirst().get();
    }

    public Resource getResource(String trait) {
        return resources.stream().filter(r -> r.hasTrait(trait)).findFirst().get();
    }

    public List<Resource> getResources() {
        return new ArrayList<>(resources);
    }

    public List<Resource> getResources(String trait) {
        return resources.stream().filter(r -> r.hasTrait(trait)).collect(Collectors.toList());
    }

    public boolean hasTrait(String name) {
        if (!Trait.exist(name))
            throw new IllegalArgumentException(name + " not created");

        return traitMap.containsKey(name);
    }

    public boolean hasTrait(String name, int value) {
        if (!Trait.exist(name))
            throw new IllegalArgumentException(name + " not created");

        if (!traitMap.containsKey(name)) {
            return false;
        }

        return traitMap.get(name).getValue() == value;
    }

    public boolean hasTraitLessThanOrEqual(String name, int value) {
        if (!Trait.exist(name))
            throw new IllegalArgumentException(name + " not created");

        if (!traitMap.containsKey(name)) {
            return false;
        }

        return traitMap.get(name).getValue() <= value;
    }

    public boolean hasTraitGreaterThanOrEqual(String name, int value) {
        if (!Trait.exist(name))
            throw new IllegalArgumentException(name + " not created");

        if (!traitMap.containsKey(name)) {
            return false;
        }

        return traitMap.get(name).getValue() >= value;
    }

    public Trait getTrait(String name) {
        Trait trait = null;

        if (traitMap.containsKey(name)) {
            trait = traitMap.get(name);
        }

        return trait;
    }

    private Trait getTraitOrCreate(String name) {
        Trait trait;

        if (traitMap.containsKey(name)) {
            trait = traitMap.get(name);
        } else {
            trait = Trait.get(name);
            traitMap.put(name, trait);
        }

        return trait;
    }

    public void incrementTrait(String name, int i, int max) {
        Trait trait = getTraitOrCreate(name);

        if (max >= 0) {
            trait.increment(i, max);
        } else {
            trait.increment(i);
        }
    }

    public void incrementTrait(String name, int i) {
        Trait trait = getTraitOrCreate(name);
        trait.increment(i);
    }

    public void decrementTrait(String name, int i, int min) {
        Trait trait = getTraitOrCreate(name);

        if (min >= 0) {
            trait.decrement(i, min);
        } else {
            trait.decrement(i);
        }

        if (trait.getValue() == 0 && trait.getTraitDescription().isRemoveAtZero()) {
            this.removeTrait(name);
        }
    }

    public void addTrait(String name, int value) {
        Trait trait = getTraitOrCreate(name);
        trait.setValue(value);
    }

    public void removeTrait(String name) {
        traitMap.remove(name);
    }

    public abstract String getDescription();

    public Collection<Trait> getTraits() {
        return traitMap.values();
    }
}