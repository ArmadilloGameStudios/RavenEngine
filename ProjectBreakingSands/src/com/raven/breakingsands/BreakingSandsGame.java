package com.raven.breakingsands;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.launcher.GameLauncher;
import com.raven.engine.scene.Scene;

public class BreakingSandsGame extends Game<BreakingSandsGame> {

    public static void main(String[] args) {
        GameLauncher.Open(new BreakingSandsGame());
        System.out.println("Lunched");

    }

    @Override
    public void setup() {
    }

    @Override
    public void breakdown() {
        setRunning(false);
    }

    @Override
    public Scene loadInitialScene() {
        return new MainMenuScene(this);
    }

    @Override
    public String getTitle() {
        return "Breaking Sands";
    }

    @Override
    public String getMainDirectory() {
        return "ProjectBreakingSands";
    }
}
