package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.engine2d.ui.UIButton;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.ui.UIContainer;

public class ExitButton
        extends UIButton<MainMenuScene, UIContainer<MainMenuScene>> {

    public ExitButton(MainMenuScene scene) {
        super(scene, "quit");
    }

    @Override
    public float getHeight() {
        return getSpriteAnimationState().getHeight();
    }

    @Override
    public float getWidth() {
        return getSpriteAnimationState().getWidth();
    }

    @Override
    public void handleMouseClick() {
        getScene().getGame().exit();
    }
}
