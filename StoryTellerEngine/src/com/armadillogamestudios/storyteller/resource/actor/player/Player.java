package com.armadillogamestudios.storyteller.resource.actor.player;

import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.storyteller.resource.actor.Actor;
import com.armadillogamestudios.storyteller.resource.item.Item;
import com.armadillogamestudios.storyteller.scenario.prompt.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.Arrays;
import java.util.List;

public abstract class Player extends Actor {

    private List<Item> potentialLoot;

    public Player(String layout) {
        super("", layout, null);
    }

    public abstract Prompt nextPrompt();
}