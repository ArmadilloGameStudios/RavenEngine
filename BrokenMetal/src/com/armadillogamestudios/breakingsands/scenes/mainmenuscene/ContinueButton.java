package com.armadillogamestudios.breakingsands.scenes.mainmenuscene;

import com.armadillogamestudios.engine2d.ui.UITextButton;

public class ContinueButton
        extends UITextButton<MainMenuScene> {


    public ContinueButton(MainMenuScene scene) {
        super(scene, "Continue", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        getScene().getGame().loadGame();
    }
}
