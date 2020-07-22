package com.armadillogamestudios.breakingsands.scenes.mainmenuscene;

import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.breakingsands.scenes.recordsscene.RecordsScene;
import com.armadillogamestudios.engine2d.ui.UITextButton;

public class RecordsButton
        extends UITextButton<MainMenuScene> {

    public RecordsButton(MainMenuScene scene) {
        super(scene, "records", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BrokenMetalGame game = getScene().getGame();

        game.prepTransitionScene(new RecordsScene(game));
    }
}
