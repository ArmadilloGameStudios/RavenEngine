package com.raven.engine2d.ui;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector4f;

import java.awt.*;

public class UILabel<S extends Scene, P extends UIContainer<S>>
        extends UIText<S, P> {

    private int width, height;
    private float x, y;


    public UILabel(S scene, GameData data, int width, int height) {
        super(scene, data);

        this.width = width;
        this.height = height;
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getYOffset() {
        return getParent().getYOffset() + y;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
    }

    @Override
    public float getXOffset() {
        return getParent().getXOffset() + x;
    }

    @Override
    public void setXOffset(float x) {
        this.x = x;
    }
}
