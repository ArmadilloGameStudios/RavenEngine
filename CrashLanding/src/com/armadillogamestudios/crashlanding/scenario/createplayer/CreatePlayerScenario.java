package com.armadillogamestudios.crashlanding.scenario.createplayer;

import com.armadillogamestudios.crashlanding.player.CrashLandingPlayer;
import com.armadillogamestudios.crashlanding.world.CrashLandingWorld;
import com.armadillogamestudios.storyteller.scenario.Scenario;

public class CreatePlayerScenario extends Scenario {

    public CreatePlayerScenario() {
        super(new CrashLandingPlayer(), new CrashLandingWorld());

        setActivePrompt("create_player_who");
        getPrompt().setTargets(getPlayer());
    }
}
