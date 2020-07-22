package com.armadillogamestudios.crashlanding.scenario.createplayer;

import com.armadillogamestudios.crashlanding.player.CrashLandingPlayer;
import com.armadillogamestudios.crashlanding.world.CrashLandingWorld;
import com.armadillogamestudios.storyteller.scenario.Scenario;
import com.armadillogamestudios.storyteller.scenario.prompt.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.Arrays;
import java.util.function.Supplier;

public class CreatePlayerScenario extends Scenario {

    public CreatePlayerScenario() {
        super(new CrashLandingPlayer(), new CrashLandingWorld());

        setActivePrompt("born_create_player");
        getPrompt().setTargets(getPlayer());
    }
}
