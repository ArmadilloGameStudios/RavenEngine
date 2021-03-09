package com.armadillogamestudios.tactics.gameengine.game;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.tactics.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;
import com.armadillogamestudios.tactics.gameengine.scene.splashscreen.SplashScreenScene;

public abstract class TacticsGame<G extends TacticsGame<G>>
        extends Game<G> {

    public static <G extends TacticsGame<G>> GameEngine<G> Launch(G game) {
        return GameEngine.Launch(game);
    }

    @Override
    public final void setup() {

    }

    @Override
    public final void breakdown() {

    }

    @Override
    public final TacticsScene<G> loadInitialScene() {
        return new SplashScreenScene<>((G) this);
    }

    @Override
    public boolean saveGame() {
        return false;
    }

    @Override
    public boolean loadGame() {
        return false;
    }

    public abstract MainMenuScene<G> getMainMenuScene();

    public abstract Highlight getTextHighlight();

    public abstract TacticsScene<G> getNewGameScene();
}
