package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

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

public abstract class MainMenuScene<S extends StoryTeller<S>> extends SceneStoryTeller<S> implements KeyboardHandler {

    private String[] savedGames;

    public MainMenuScene(S game) {
        super(game);
    }

    @Override
    public void loadShaderTextures() {
        List<ShaderTexture> textures = getShaderTextures();

        // textures.addAll(DisplayPawn.getSpriteSheets(this));
    }

    @Override
    public final void onEnterScene() {
        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

        File sFile = charPath.toFile();
        if (sFile.list() == null) {
            sFile.mkdir();
        }

        savedGames = sFile.list();

        loadUI();

        addKeyboardHandler(this);
    }

    protected abstract void loadUI();

    public final boolean isLoadGame() {
        return savedGames != null && savedGames.length > 0;
    }

    public final void onLoadGameClick() {

    }

    public final void onNewGameClick() {
        getGame().prepTransitionScene(new ScenarioScene<>(getGame(), true));
    }

    public final void onSettingsClick() {
    }

    public final void onCreditsClick() {
    }

    public void onExitClick() {
        getGame().exit();
    }

    @Override
    public void onInputKey(KeyData keyData) {
        if (keyData.getKey() ==  KeyData.Key.ESCAPE) {
            getGame().exit();
        }
    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }
}
