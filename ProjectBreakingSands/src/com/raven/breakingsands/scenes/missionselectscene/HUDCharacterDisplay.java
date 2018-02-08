package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDText;
import com.raven.engine.worldobject.TextObject;

import java.awt.*;

public class HUDCharacterDisplay extends HUDText<MissionSelectScene, HUDCharacterSelect> {

    private float x, y;

    private TextObject textObject;

    private String text = "";

    public HUDCharacterDisplay(MissionSelectScene scene) {
        super(scene);

        this.text = text;

        textObject = new TextObject((int)getWidth(), (int)getHeight());
        textObject.setFont(new Font( "SansSerif", Font.PLAIN, 20));
        textObject.setText(text);

        this.setTexture(textObject);
    }


    @Override
    public void release() {
        super.release();

        textObject.release();
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public float getHeight() {
        return 75;
    }

    @Override
    public float getWidth() {
        return 250;
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

    public void setText(String text) {
        this.text = text;
        textObject.setText(text);
    }
}
