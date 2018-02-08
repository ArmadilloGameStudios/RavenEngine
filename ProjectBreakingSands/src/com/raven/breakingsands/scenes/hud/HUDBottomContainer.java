package com.raven.breakingsands.scenes.hud;

import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDObject;

import java.util.List;

public class HUDBottomContainer<S extends Scene> extends HUDContainer<S> {

    private float x, y;

    private Vector4f color = new Vector4f(.25f,.25f,.25f,.5f);

    public HUDBottomContainer(S scene) {
        super(scene);
    }

    @Override
    public float getBorder() {
        return 5f;
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
    public float getYOffset() {
        return getHeight() + y;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
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
