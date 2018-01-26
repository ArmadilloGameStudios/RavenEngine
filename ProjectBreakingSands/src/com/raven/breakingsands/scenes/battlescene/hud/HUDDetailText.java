package com.raven.breakingsands.scenes.battlescene.hud;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDText;

public class HUDDetailText extends HUDText<BattleScene, HUDContainer<BattleScene>> {

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
        return 85;
    }

    @Override
    public float getWidth() {
        return 85;
    }

    @Override
    public float getYOffset() {
        return getParent().getYOffset();
    }

    @Override
    public float getXOffset() {
        return getParent().getXOffset();
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
