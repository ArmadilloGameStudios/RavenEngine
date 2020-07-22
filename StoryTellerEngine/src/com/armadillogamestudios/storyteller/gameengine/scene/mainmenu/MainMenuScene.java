package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class MainMenuScene<S extends StoryTeller<S>> extends Scene<S> {

    // private DisplayPawn pawn;

    public MainMenuScene(S game) {
        super(game);
    }

    @Override
    public void loadShaderTextures() {
        List<ShaderTexture> textures = getShaderTextures();

        // textures.addAll(DisplayPawn.getSpriteSheets(this));
    }

    @Override
    public void onEnterScene() {

        // Pawn
//        pawn = new DisplayPawn(this);
//        addChild(pawn);

        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        // UI
        UIContainer<MainMenuScene> container = new UIContainer<>(this, UIContainer.Location.UPPER_RIGHT, UIContainer.Layout.VERTICAL);
        addChild(container);

        NewGameButton<S> newGameBtn = new NewGameButton<>(this);
        newGameBtn.load();
        container.addChild(newGameBtn);

        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

        File sFile = charPath.toFile();
        if (sFile != null) {
            if (sFile.list() == null) {
                sFile.mkdir();
            }

            String[] files = sFile.list();
            if (files != null && files.length > 0) {
                LoadGameButton loadBtn = new LoadGameButton(this, files);
                loadBtn.load();
                container.addChild(loadBtn);
            }
        }

        SettingsButton settingsButton = new SettingsButton(this);
        settingsButton.load();
        container.addChild(settingsButton);

        CreditsButton creditsButton = new CreditsButton(this);
        creditsButton.load();
        container.addChild(creditsButton);

        ExitButton exitBtn = new ExitButton(this);
        exitBtn.load();
        container.addChild(exitBtn);

        container.pack();

        addChild(getGame().getMainMenuDecor(this));
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void inputKey(int key, int action, int mods) {
        if (GLFW.GLFW_KEY_ESCAPE == key && GLFW.GLFW_PRESS == action) {
            getGame().exit();
        }
    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }
}
