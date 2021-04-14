package com.armadillogamestudios.storyteller.gameengine.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.scenario.ScenarioScene;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class MainMenuScene<G extends StoryTeller<G>> extends SceneStoryTeller<G> implements KeyboardHandler {

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
        getGame().prepTransitionScene(getGame().getCreatePlayerScene());
    }

    public final void onSettingsClick() {
    }

    public final void onCreditsClick() {
    }

    public void onExitClick() {
        getGame().exit();
    }
}
