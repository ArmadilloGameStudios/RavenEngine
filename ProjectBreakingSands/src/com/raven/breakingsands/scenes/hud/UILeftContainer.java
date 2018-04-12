package com.raven.breakingsands.scenes.hud;

import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIContainer;

public abstract class UILeftContainer<S extends Scene>
        extends UIContainer<S> {

    private float x, y;

    public UILeftContainer(S scene) {
        super(scene);
    }

    @Override
    public float getXOffset() {
        return getWidth() / 2f + x;
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
}
