package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;

public class CreditsButton
        extends UITextButton<MainMenuScene<? extends StoryTeller<?>>> {

    public CreditsButton(MainMenuScene<?> scene) {
        super(scene, "credits", "sprites/button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        StoryTeller game = getScene().getGame();

        // game.prepTransitionScene(new CreditsScene(game));
    }
}
