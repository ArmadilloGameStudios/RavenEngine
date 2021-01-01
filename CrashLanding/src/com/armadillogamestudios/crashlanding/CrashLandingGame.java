package com.armadillogamestudios.crashlanding;

import com.armadillogamestudios.crashlanding.scenario.createplayer.CreatePlayerScenario;
import com.armadillogamestudios.crashlanding.scene.mainmenu.CrashLandingMainMenuScene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.storyteller.scenario.Scenario;

import java.util.Arrays;
import java.util.List;

public class CrashLandingGame extends StoryTeller<CrashLandingGame> {

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
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
    public MainMenuScene<CrashLandingGame> getMainMenuScene() {
        return new CrashLandingMainMenuScene(this);
    }

    @Override
    public Highlight getTextHighlight() {
        return TEXT;
    }

    @Override
    public List<String> getTraitOrder() {
        return Arrays.asList("player", "age",  "status", "personality", "skill");
    }
}
