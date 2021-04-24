package com.armadillogamestudios.reclaim;

import com.armadillogamestudios.reclaim.data.ReclaimActiveGameData;
import com.armadillogamestudios.reclaim.data.Player;
import com.armadillogamestudios.reclaim.data.World;
import com.armadillogamestudios.reclaim.scene.mainmenu.ReclaimMainMenuScene;
import com.armadillogamestudios.reclaim.scene.world.ReclaimWorldScene;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;
import com.armadillogamestudios.tactics.gameengine.scene.mainmenu.MainMenuScene;

import java.util.Random;

public class ReclaimGame extends TacticsGame<ReclaimGame> {

    private static final int seed = new Random().nextInt();
    public static final Random RANDOM = new Random(seed);

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
    private static final String mainDirectory = "CosmicExile";
    private static final String title = "Reclaim";

    private static ReclaimActiveGameData activeGameData;

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
    public MainMenuScene<ReclaimGame> getMainMenuScene() {
        return new ReclaimMainMenuScene(this);
    }

    @Override
    public Highlight getTextHighlight() {
        return TEXT;
    }

    @Override
    public TacticsScene<ReclaimGame> getNewGameScene() {
        activeGameData = new ReclaimActiveGameData();

        setStartingData();

        return new ReclaimWorldScene(this);
    }

    private void setStartingData() {
        activeGameData.addPlayer(new Player());
        activeGameData.setWorld(new World());
    }

    public static ReclaimActiveGameData getActiveGameData() {
        return activeGameData;
    }
}