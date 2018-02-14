package com.raven.engine.worldobject;

import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;

import java.awt.*;

public class HUDLabel<S extends Scene, P extends HUDContainer<S>>
        extends HUDText<S, P> {

    private int width, height;
    private float x, y;

    private TextObject textObject;

    public HUDLabel(S scene, String text, int width, int height) {
        super(scene);

        this.width = width;
        this.height = height;

        textObject = new TextObject(width, height);
        textObject.setText(text);
        this.setTexture(textObject);
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

    @Override
    public Vector4f getColor() {
        return null;
    }

    @Override
    public void setText(String text) {
        getTexture().setText(text);
    }

    public void setFont(Font font) {
        getTexture().setFont(font);
    }

    @Override
    public boolean doBlend() {
        return false;
    }
}
