package com.armadillogamestudios.reclaim;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.graphicspipeline.GraphicsPipeline;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.reclaim.data.ReclaimGameData;
import com.armadillogamestudios.reclaim.graphicspipeline.SagaGraphicsPipeline;
import com.armadillogamestudios.reclaim.scene.SagaScene;
import com.armadillogamestudios.reclaim.scene.mainmenu.ReclaimMainMenuScene;
import com.armadillogamestudios.reclaim.scene.splashscreen.SplashScreenScene;
import com.armadillogamestudios.reclaim.scene.world.ReclaimWorldScene;

import java.util.Random;

public class ReclaimGame extends Game<ReclaimGame> {

    private static final int seed = new Random().nextInt();
    public static final Random RANDOM = new Random(seed);

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
    private static final String mainDirectory = "CosmicExile";
    private static final String title = "Reclaim";
    private ReclaimGameData reclaimGameData;

    public static void main(String[] args) {

        System.out.println("Lunching Reclaim");
        System.out.println("Seed " + seed);

        GameEngine.Launch(new ReclaimGame());
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

    public final ReclaimMainMenuScene getMainMenuScene() {
        return new ReclaimMainMenuScene(this);
    }

    public final Highlight getTextHighlight() {
        return TEXT;
    }

    public final SagaScene getNewGameScene() {
        setStartingData();

        return new ReclaimWorldScene(this);
    }

    private void setStartingData() {
        reclaimGameData = new ReclaimGameData(getEngine().getGameDatabase());
    }

    public ReclaimGameData getReclaimGameData() {
        return reclaimGameData;
    }
}