package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;

public class SettingsButton
        extends UITextButton<MainMenuScene<? extends StoryTeller<?>>> {

    public SettingsButton(MainMenuScene<?> scene) {
        super(scene, "settings", "sprites/button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        StoryTeller game = getScene().getGame();

        // game.prepTransitionScene(new SettingsScene(game));
    }
}
