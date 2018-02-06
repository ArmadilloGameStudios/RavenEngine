package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;
import com.raven.engine.worldobject.MouseHandler;
import com.raven.engine.worldobject.TextObject;

import java.awt.*;

public abstract class MenuButton extends HUDText<BattleScene, Menu>
        implements MouseHandler {

    private float x, y;

    private TextObject defaultText, hoverText;

    private String text;

    public MenuButton(BattleScene scene, String text) {
        super(scene);

        this.text = text;

        defaultText = new TextObject((int)getWidth(), (int)getHeight());
        defaultText.setFont(new Font( "SansSerif", Font.PLAIN, 20));
        defaultText.setText(text);

        this.setTexture(defaultText);

        hoverText = new TextObject((int)getWidth(), (int)getHeight());
        hoverText.setFont(new Font( "SansSerif", Font.PLAIN, 20));
        hoverText.setBackgroundColor(new Vector4f(.1f, .6f, .9f, 1f));
        hoverText.setText(text);

        this.addMouseHandler(this);
    }

    @Override
    public void release() {
        super.release();

        defaultText.release();
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
    public void handleMouseEnter() {
        this.setTexture(hoverText);
    }

    @Override
    public void handleMouseLeave() {
        this.setTexture(defaultText);
    }

    @Override
    public void handleMouseHover(float delta) {

    }
}
