package com.armadillogamestudios.storyteller.resource.item;

import com.armadillogamestudios.storyteller.resource.Resource;

public class Item extends Resource {

    private int weight = 10;
    private String description;

    public Item(String name, String description) {
        super(name, "item");

        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}