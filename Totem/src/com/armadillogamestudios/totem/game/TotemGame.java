package com.armadillogamestudios.totem.game;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.graphicspipeline.GraphicsPipeline;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.totem.game.scene.TotemScene;
import com.armadillogamestudios.totem.game.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.totem.game.scene.splashscreen.SplashScreenScene;
import com.armadillogamestudios.totem.game.scene.testspell.TestSpellScene;

import java.util.Arrays;

public class TotemGame extends Game<TotemGame> {

    private static final String mainDirectory = "Totem";
    private static final String title = "Totem";

    public static void main(String[] args) {
        System.out.println("Lunching Totem");
        GameEngine.Launch(new TotemGame());
    }

    @Override
    public void setup() {

    }

    @Override
    public void breakdown() {

    }

    @Override
    public final TotemScene loadInitialScene() {
        return new SplashScreenScene(this);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getMainDirectory() {
        return mainDirectory;
    }

    @Override
    public boolean saveGame() {
        return false;
    }

    @Override
    public boolean loadGame() {
        return false;
    }

    @Override
    public GraphicsPipeline createGraphicsPipeline(GameWindow window) {
        GraphicsPipeline pipeline = new GraphicsPipeline(this, window);

        pipeline.drawIDMap();

        pipeline.drawLayers(
                Layer.Destination.Terrain,
                Layer.Destination.Details,
                Layer.Destination.Effects,
                Layer.Destination.UI,
                Layer.Destination.ToolTip);

        pipeline.addCompilationShader(
                Arrays.asList(
                        Layer.Destination.Terrain,
                        Layer.Destination.Details,
                        Layer.Destination.Effects),
                Layer.Destination.UI,
                Arrays.asList(
                        Layer.Destination.Lines,
                        Layer.Destination.ToolTip)
        );

        return pipeline;
    }

    public Scene<TotemGame> getMainMenuScene() {
        return new MainMenuScene(this);
    }

    public Scene<TotemGame> getNewGameScene() {
        return new TestSpellScene(this);
    }
}
