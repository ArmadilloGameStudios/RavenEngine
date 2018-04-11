package com.raven.breakingsands.scenes.battlescene;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.ui.UIText;

public class UIDetailText
        extends UIText<BattleScene, UIContainer<BattleScene>> {

    private float x, y;

    private Vector4f color = new Vector4f();

    public UIDetailText(BattleScene scene) {
        super(scene, new GameData("text"));
    }

    @Override
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public float getHeight() {
        return 250;
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
        return color;
    }

    @Override
    public boolean doBlend() {
        return false;
    }
}
