package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.UIButton;
import com.raven.engine2d.ui.UIContainer;

public class ContinueButton
        extends UIButton<MainMenuScene, UIContainer<MainMenuScene>> {


    public ContinueButton(MainMenuScene scene) {
        super(scene, "Continue");
    }

    @Override
    public float getHeight() {
        return 75f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }

    @Override
    public void handleMouseClick() {
        getScene().getGame().loadGame();
    }
}
