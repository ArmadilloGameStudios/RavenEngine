package com.armadillogamestudios.cosmicexile.game;

import com.armadillogamestudios.cosmicexile.data.contract.Contract;
import com.armadillogamestudios.cosmicexile.data.CosmicExileActiveGameData;
import com.armadillogamestudios.cosmicexile.scene.mainmenu.CosmicExileMainMenuScene;
import com.armadillogamestudios.cosmicexile.scene.ship.CosmicExileShipScene;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;
import com.armadillogamestudios.tactics.gameengine.scene.mainmenu.MainMenuScene;

import java.util.Random;

public class CosmicExileGame extends TacticsGame<CosmicExileGame> {

    public static final Random RANDOM = new Random();

    private static final Highlight TEXT = new Highlight(.4f, .4f, .4f, .6f);
    private static final String mainDirectory = "CosmicExile";
    private static final String title = "Cosmic Exile";

    private CosmicExileActiveGameData activeGameData;

    public static void main(String[] args) {
        TacticsGame.Launch(new CosmicExileGame());
        System.out.println("Lunched Cosmic Exile");
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
    public MainMenuScene<CosmicExileGame> getMainMenuScene() {
        return new CosmicExileMainMenuScene(this);
    }

    @Override
    public Highlight getTextHighlight() {
        return TEXT;
    }

    @Override
    public TacticsScene<CosmicExileGame> getNewGameScene() {
        activeGameData = new CosmicExileActiveGameData();
        setStartingData();
        return new CosmicExileShipScene(this);
    }

    private void setStartingData() {
        activeGameData.addContract(new Contract("Test Battle", "description"));
    }

    public CosmicExileActiveGameData getActiveGameData() {
        return activeGameData;
    }
}