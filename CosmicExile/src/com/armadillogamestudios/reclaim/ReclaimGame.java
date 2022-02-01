package com.armadillogamestudios.reclaim;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.reclaim.data.ReclaimGameData;
import com.armadillogamestudios.reclaim.scene.mainmenu.ReclaimMainMenuScene;
import com.armadillogamestudios.reclaim.scene.world.ReclaimWorld;
import com.armadillogamestudios.reclaim.scene.world.ReclaimWorldScene;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;
import com.armadillogamestudios.tactics.gameengine.scene.mainmenu.MainMenuScene;
import com.armadillogamestudios.tactics.gameengine.scene.splashscreen.SplashScreenScene;

import java.util.Random;

public class ReclaimGame extends TacticsGame<ReclaimGame> {

    private static final int seed = new Random().nextInt();
    public static final Random RANDOM = new Random(seed);

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
    private static final String mainDirectory = "CosmicExile";
    private static final String title = "Reclaim";
    private ReclaimGameData reclaimGameData;

    public static void main(String[] args) {
        System.out.println("Lunching Reclaim");
        System.out.println("Seed " + seed);
        TacticsGame.Launch(new ReclaimGame());
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
    public TacticsScene<ReclaimGame> loadInitialScene() {
        return getNewGameScene();
    }

    @Override
    public MainMenuScene<ReclaimGame> getMainMenuScene() {
        return new ReclaimMainMenuScene(this);
    }

    @Override
    public Highlight getTextHighlight() {
        return TEXT;
    }

    @Override
    public TacticsScene<ReclaimGame> getNewGameScene() {
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