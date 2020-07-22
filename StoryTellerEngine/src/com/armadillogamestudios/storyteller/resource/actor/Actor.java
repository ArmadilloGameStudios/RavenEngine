package com.armadillogamestudios.storyteller.resource.actor;

import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.location.Location;
import com.armadillogamestudios.storyteller.resource.location.Place;
import com.armadillogamestudios.storyteller.resource.relationship.ResourceRelationship;
import com.armadillogamestudios.storyteller.resource.trait.Trait;

import java.util.stream.Collectors;

public abstract class Actor extends Resource {
    public Actor(String name, String layout, Place location) {
        super(name, layout);

        ResourceRelationship.create(this, location, ResourceRelationship.Type.ON);
    }

    @Override
    public String getDescription() {
        String des = getTraits().stream().map(Trait::toDefaultString).collect(Collectors.joining("\n"));

        return des;
    }
}