package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;
import com.raven.engine.worldobject.TextObject;

import java.awt.*;

public class ExitButton extends HUDText<MainMenuScene, HUDContainer<MainMenuScene>> {

    private float x, y;

    private TextObject text;


    public ExitButton(MainMenuScene scene) {
        super(scene);

        text = new TextObject((int)getWidth(), (int)getHeight());
        text.setFont(new Font( "SansSerif", Font.PLAIN, 20));
        text.setText("Exit");

        this.setTexture(text);
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
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
    public boolean doBlend() {
        return false;
    }
}
