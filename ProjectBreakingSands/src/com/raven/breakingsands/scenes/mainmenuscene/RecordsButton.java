package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.recordsscene.RecordsScene;
import com.raven.engine2d.ui.UITextButton;

import java.util.List;

public class RecordsButton
        extends UITextButton<MainMenuScene> {

    public RecordsButton(MainMenuScene scene) {
        super(scene, "records", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.prepTransitionScene(new RecordsScene(game));
    }
}
