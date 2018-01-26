package com.raven.breakingsands.scenes.hud;

import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;

public class HUDBottomContainer extends HUDContainer {

    private Vector4f color = new Vector4f(.25f,.25f,.25f,.5f);

    public HUDBottomContainer(Scene scene) {
        super(scene);
    }

    @Override
    public float getHeight() {
        return 100f;
    }

    @Override
    public float getWidth() {
        return 100f;
    }

    @Override
    public float getXOffset() {
        return 0f;
    }

    @Override
    public float getYOffset() {
        return getHeight();
    }

    @Override
    public int getStyle() {
        return 1;
    }

    @Override
    public Vector4f getColor() {
        return color;
    }

    @Override
    public boolean doBlend() {
        return true;
    }
}
