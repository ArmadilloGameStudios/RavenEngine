package com.armadillogamestudios.tactics.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.scene.TacticsScene;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class MainMenuScene<G extends TacticsGame<G>> extends TacticsScene<G> implements KeyboardHandler {

    private String[] savedGames;

    public MainMenuScene(G game) {
        super(game);

        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

        File sFile = charPath.toFile();
        if (sFile.list() == null) {
            sFile.mkdir();
        }

        savedGames = sFile.list();

        addKeyboardHandler(this);
    }

    @Override
    public final void onKeyPress(KeyData keyData) {
        if (keyData.getKey() ==  KeyData.Key.ESCAPE) {
            getGame().exit();
        }
    }

    public final boolean isLoadGame() {
        return savedGames != null && savedGames.length > 0;
    }

    public final void onLoadGameClick() {

    }

    public final void onNewGameClick() {
        getGame().prepTransitionScene(getGame().getNewGameScene());
    }

    public final void onSettingsClick() {
    }

    public final void onCreditsClick() {
    }

    public void onExitClick() {
        getGame().exit();
    }
}
