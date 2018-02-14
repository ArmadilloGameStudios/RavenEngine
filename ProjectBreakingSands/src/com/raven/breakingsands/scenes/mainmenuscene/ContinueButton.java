package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.engine.scene.Layer;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.*;

import java.awt.*;

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
