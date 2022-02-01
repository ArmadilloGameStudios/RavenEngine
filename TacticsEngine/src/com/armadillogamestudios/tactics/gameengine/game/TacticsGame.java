package com.armadillogamestudios.tactics.gameengine.game;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.graphicspipeline.GraphicsPipeline;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.tactics.gameengine.game.graphicspipeline.TacticsGraphicsPipeline;
import com.armadillogamestudios.tactics.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;
import com.armadillogamestudios.tactics.gameengine.scene.splashscreen.SplashScreenScene;

import java.util.Arrays;
import java.util.Collections;

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
    public TacticsScene<G> loadInitialScene() {
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

    @Override
    public GraphicsPipeline createGraphicsPipeline(GameWindow window) {
        return TacticsGraphicsPipeline.createPipeline(this, window);
    }
}
