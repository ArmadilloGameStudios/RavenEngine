package com.armadillogamestudios.storyteller.resource.actor.player;

import com.armadillogamestudios.storyteller.resource.actor.Actor;
import com.armadillogamestudios.storyteller.resource.item.Item;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.List;

public abstract class Player extends Actor {

    private List<Item> potentialLoot;

    public Player(String layout) {
        super("", layout, null);
    }

    public abstract Prompt nextPrompt();
}