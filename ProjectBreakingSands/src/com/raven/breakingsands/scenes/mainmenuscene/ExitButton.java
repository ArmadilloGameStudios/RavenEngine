package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.UIButton;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.UIContainer;

public class ExitButton
        extends UIButton<MainMenuScene, UIContainer<MainMenuScene>> {

    public ExitButton(MainMenuScene scene) {
        super(scene, new GameData("Exit"));
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
