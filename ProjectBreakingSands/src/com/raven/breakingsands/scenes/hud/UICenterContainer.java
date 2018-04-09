package com.raven.breakingsands.scenes.hud;

import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIContainer;

public abstract class UICenterContainer<S extends Scene>
        extends UIContainer<S> {

    private float x, y;

    public UICenterContainer(S scene) {
        super(scene);
    }

    @Override
    public float getBorder() {
        return 5f;
    }

    @Override
    public int getStyle() {
        return 0;
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
    public float getXOffset() {
        return x;
    }

    @Override
    public void setXOffset(float x) {
        this.x = x;
    }

    @Override
    public boolean doBlend() {
        return true;
    }
}
