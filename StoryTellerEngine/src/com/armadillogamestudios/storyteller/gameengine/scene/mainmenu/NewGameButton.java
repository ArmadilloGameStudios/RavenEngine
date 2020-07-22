package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.scenario.ScenarioScene;

public class NewGameButton<S extends StoryTeller<S>>
        extends UITextButton<MainMenuScene<S>> {

    public NewGameButton(MainMenuScene<S> scene) {
        super(scene, "new game", "sprites/button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
       S game = getScene().getGame();

       game.prepTransitionScene(new ScenarioScene<>(game, true));
    }
}
