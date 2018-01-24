package com.raven.engine.worldobject;

import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;

public class HUDObject<S extends Scene> implements Childable<Layer<HUDObject>> {

    private S scene;
    private Layer<HUDObject> layer;
    private float x_scale, y_scale;

    public HUDObject(S scene, float x_scale, float y_scale) {
        this.scene = scene;

        this.x_scale = x_scale;
        this.y_scale = y_scale;
    }

    public float getScaleY() {
        return y_scale;
    }

    public float getScaleX() {
        return x_scale;
    }

    @Override
    public void setParent(Layer<HUDObject> parent) {
        layer = parent;
    }

    @Override
    public Layer<HUDObject> getParent() {
        return layer;
    }

    @Override
    public void update(float delta) {

    }
}
