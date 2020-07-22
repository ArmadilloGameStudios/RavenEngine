package com.armadillogamestudios.crashlanding;

import com.armadillogamestudios.crashlanding.scenario.createplayer.CreatePlayerScenario;
import com.armadillogamestudios.crashlanding.ui.CrashLandingMainMenuDecor;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.storyteller.gameengine.scene.scenario.ScenarioScene;
import com.armadillogamestudios.storyteller.resource.actor.player.Player;
import com.armadillogamestudios.storyteller.resource.trait.Trait;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;
import com.armadillogamestudios.storyteller.scenario.Scenario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CrashLandingGame extends StoryTeller<CrashLandingGame> {

    private static final String mainDirectory = "CrashLanding";

    public static void main(String[] args) {
        StoryTeller.Launch(new CrashLandingGame());
        System.out.println("Lunched Crash Landing");
    }

    @Override
    public String getTitle() {
        return "Crash Landing";
    }

    @Override
    public String getMainDirectory() {
        return mainDirectory;
    }

    @Override
    public Scenario startScenario() {
        return new CreatePlayerScenario();
    }

    @Override
    public CrashLandingMainMenuDecor getMainMenuDecor(MainMenuScene<CrashLandingGame> scene) {
        return new CrashLandingMainMenuDecor(scene);
    }
}
