package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.util.Vector3f;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.TextObject;

import java.awt.*;

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
