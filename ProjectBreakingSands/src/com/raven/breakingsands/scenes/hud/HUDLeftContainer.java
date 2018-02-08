package com.raven.breakingsands.scenes.hud;

import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;

public abstract class HUDLeftContainer<S extends Scene> extends HUDContainer<S> {

    private float x, y;

    public HUDLeftContainer(S scene) {
        super(scene);
    }

    @Override
    public float getBorder() {
        return 5f;
    }

    @Override
    public float getXOffset() {
        return getWidth() + x;
    }

    @Override
    public void setXOffset(float x) {
        this.x = x;
    }

    @Override
    public float getYOffset() {
        return y;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
    }

    @Override
    public int getStyle() {
        return 2;
    }

    @Override
    public boolean doBlend() {
        return true;
    }
}
