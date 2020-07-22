package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;

public class LoadGameButton
        extends UITextButton<MainMenuScene<? extends StoryTeller<?>>> {

    private String[] files;

    public LoadGameButton(MainMenuScene<?> scene, String[] files) {
        super(scene, "Continue", "sprites/button.png", "mainbutton");

        this.files = files;
    }

    @Override
    public void handleMouseClick() {
        getScene().getGame().loadGame();
    }
}
