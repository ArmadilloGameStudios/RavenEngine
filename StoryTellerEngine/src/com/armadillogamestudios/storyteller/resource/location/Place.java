package com.armadillogamestudios.storyteller.resource.location;

public abstract class Place extends Location<Region> {
    public Place(String name, Region region) {
        super(name, region);
    }
}