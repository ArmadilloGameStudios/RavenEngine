package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.engine.worldobject.HUDContainer;

public class ExitButton
        extends HUDButton<MainMenuScene, HUDContainer<MainMenuScene>> {

    public ExitButton(MainMenuScene scene) {
        super(scene, "Exit");
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
        getScene().getGame().exit();
    }
}
