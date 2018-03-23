package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.engine.worldobject.*;

public class ContinueButton
        extends HUDButton<MainMenuScene, HUDContainer<MainMenuScene>> {


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
