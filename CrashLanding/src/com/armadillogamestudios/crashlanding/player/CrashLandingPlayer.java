package com.armadillogamestudios.crashlanding.player;

import com.armadillogamestudios.storyteller.resource.actor.player.Player;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

public class CrashLandingPlayer extends Player {

    private Prompt idle, leave, pick_loot, wait, viewInventory, viewTraits;

    private Prompt activePrompt = idle;

    public CrashLandingPlayer() {
        super("player");
    }


    @Override
    public Prompt nextPrompt() {
        return activePrompt;
    }
}