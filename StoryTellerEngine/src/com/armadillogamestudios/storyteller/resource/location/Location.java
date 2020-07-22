package com.armadillogamestudios.storyteller.resource.location;

import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.relationship.ResourceRelationship;

public abstract class Location<T extends Location<?>> extends Resource {
    public Location(String name, T location) {
        super(name, "location");

        ResourceRelationship.create(this, location, ResourceRelationship.Type.PART_OF);
    }

    @Override
    public String getDescription() {
        return getName();
    }
}