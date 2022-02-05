package com.armadillogamestudios.saga;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.graphicspipeline.GraphicsPipeline;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.saga.data.SagaGameData;
import com.armadillogamestudios.saga.graphicspipeline.SagaGraphicsPipeline;
import com.armadillogamestudios.saga.scene.SagaScene;
import com.armadillogamestudios.saga.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.saga.scene.splashscreen.SplashScreenScene;
import com.armadillogamestudios.saga.scene.world.MapScene;

import java.util.Random;

public class SagaGame extends Game<SagaGame> {

    private static final int seed = new Random().nextInt();
    public static final Random RANDOM = new Random(seed);

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
    private static final String mainDirectory = "Saga";
    private static final String title = "Saga";
    private SagaGameData sagaGameData;

    public static void main(String[] args) {

        System.out.println("Lunching Saga");
        System.out.println("Seed " + seed);

        GameEngine.Launch(new SagaGame());
    }

    @Override
    public final void setup() {

    }

    @Override
    public final void breakdown() {

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
    public String getTitle() {
        return title;
    }

    @Override
    public String getMainDirectory() {
        return mainDirectory;
    }

    @Override
    public SagaScene loadInitialScene() {
        return new SplashScreenScene(this);
    }

    @Override
    public GraphicsPipeline createGraphicsPipeline(GameWindow window) {
        return SagaGraphicsPipeline.createPipeline(this, window);
    }

    public final MainMenuScene getMainMenuScene() {
        return new MainMenuScene(this);
    }

    public final Highlight getTextHighlight() {
        return TEXT;
    }

    public final SagaScene getNewGameScene() {
        setStartingData();

        return new MapScene(this);
    }

    private void setStartingData() {
        sagaGameData = new SagaGameData(getEngine().getGameDatabase());
    }

    public SagaGameData getSagaGameData() {
        return sagaGameData;
    }
}