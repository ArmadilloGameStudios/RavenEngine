package com.raven.breakingsands.scenes.battlescene;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;

public class HUDDetailText extends HUDText<BattleScene, HUDContainer<BattleScene>> {

    private float x, y;

    private Vector4f color = new Vector4f();

    public HUDDetailText(BattleScene scene) {
        super(scene);
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
