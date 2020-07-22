package com.armadillogamestudios.breakingsands.scenes.mainmenuscene;

import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.breakingsands.scenes.creditsscene.CreditsScene;
import com.armadillogamestudios.engine2d.ui.UITextButton;

public class CreditsButton
        extends UITextButton<MainMenuScene> {

    public CreditsButton(MainMenuScene scene) {
        super(scene, "credits", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BrokenMetalGame game = getScene().getGame();

        game.prepTransitionScene(new CreditsScene(game));
    }
}
