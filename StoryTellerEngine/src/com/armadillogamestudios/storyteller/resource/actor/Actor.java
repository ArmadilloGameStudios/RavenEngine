package com.armadillogamestudios.storyteller.resource.actor;

import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.location.Location;
import com.armadillogamestudios.storyteller.resource.location.Place;
import com.armadillogamestudios.storyteller.resource.relationship.ResourceRelationship;
import com.armadillogamestudios.storyteller.resource.trait.Trait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Actor extends Resource {
    public Actor(String name, String layout, Place location) {
        super(name, layout);

        ResourceRelationship.create(this, location, ResourceRelationship.Type.ON);
    }

    @Override
    public String getDescription() {
        List<Trait> sorted = new ArrayList<>(getTraits());
        sorted.sort(StoryTeller.getTraitComparator());

        String des = sorted.stream().map(Trait::toDefaultString).collect(Collectors.joining("\n"));

        return getName() + '\n' + des;
    }
}