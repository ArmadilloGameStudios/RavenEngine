package com.raven.breakingsands.scenes.battlescene.hud;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;

public class HUDBottomContainer extends HUDContainer<BattleScene> {

    private Vector4f color = new Vector4f(0,0,0,.5f);

    public HUDBottomContainer(BattleScene scene) {
        super(scene);
    }

    @Override
    public float getHeight() {
        return 100f;
    }

    @Override
    public float getWidth() {
        return 100f;
    }

    @Override
    public float getXOffset() {
        return 0f;
    }

    @Override
    public float getYOffset() {
        return getHeight();
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
