package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.engine.scene.Layer;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.*;

import java.awt.*;

public class ContinueButton
        extends HUDText<MainMenuScene, HUDContainer<MainMenuScene>>
        implements MouseHandler {

    private float x, y;

    private TextObject text, hoverText;


    public ContinueButton(MainMenuScene scene) {
        super(scene);

        text = new TextObject((int) getWidth(), (int) getHeight());
        text.setFont(new Font("SansSerif", Font.PLAIN, 20));
        text.setText("Continue");

        this.setTexture(text);

        hoverText = new TextObject((int) getWidth(), (int) getHeight());
        hoverText.setFont(new Font("SansSerif", Font.PLAIN, 20));
        hoverText.setBackgroundColor(new Vector4f(.1f, .6f, .9f, 1f));
        hoverText.setText("Continue");

        this.addMouseHandler(this);
    }

    @Override
    public void release() {
        super.release();

        text.release();
        hoverText.release();
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

    @Override
    public void handleMouseClick() {
        getScene().getGame().loadGame();
    }

    @Override
    public void handleMouseEnter() {
        this.setTexture(hoverText);
    }

    @Override
    public void handleMouseLeave() {
        this.setTexture(text);
    }

    @Override
    public void handleMouseHover(float delta) {

    }
}
