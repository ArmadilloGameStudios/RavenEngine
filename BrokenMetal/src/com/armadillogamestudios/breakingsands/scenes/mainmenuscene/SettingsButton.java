package com.armadillogamestudios.breakingsands.scenes.mainmenuscene;

import com.armadillogamestudios.breakingsands.scenes.settingsscene.SettingsScene;
import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.engine2d.ui.UITextButton;

public class SettingsButton
        extends UITextButton<MainMenuScene> {

    public SettingsButton(MainMenuScene scene) {
        super(scene, "settings", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BrokenMetalGame game = getScene().getGame();

        game.prepTransitionScene(new SettingsScene(game));
    }
}
