package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.ui.UIText;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.worldobject.MouseHandler;
import com.sun.org.glassfish.gmbal.ManagedObject;

public abstract class UIButton<S extends Scene, C extends UIContainer<S>>
        extends UIText<S, C>
        implements MouseHandler {

    private float x, y;
    private SpriteAnimationState spriteAnimationState;

    public UIButton(S scene, String text) {
        super(scene, text, "sprites/button.png");

        spriteAnimationState = new SpriteAnimationState(GameEngine.getEngine().getAnimation("newgamebutton"));

        this.addMouseHandler(this);
    }

    @Override
    public void release() {
        super.release();
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
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
    public void handleMouseEnter() {
        this.setAnimationAction("hover");
    }

    @Override
    public void handleMouseLeave() {
        this.setAnimationAction("idle");
    }

    @Override
    public void handleMouseHover(float delta) {

    }

    @Override
    public SpriteAnimationState getSpriteAnimationState() {
        return spriteAnimationState;
    }
}
