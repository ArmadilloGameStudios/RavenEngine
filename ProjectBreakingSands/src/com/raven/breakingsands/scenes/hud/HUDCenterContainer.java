package com.raven.breakingsands.scenes.hud;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.HUDContainer;

public abstract class HUDCenterContainer<S extends Scene> extends HUDContainer<S> {

    private float x, y;

    public HUDCenterContainer(S scene) {
        super(scene);
    }

    @Override
    public float getBorder() {
        return 5f;
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public float getYOffset() {
        return y;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
    }

    @Override
    public float getXOffset() {
        return x;
    }

    @Override
    public void setXOffset(float x) {
        this.x = x;
    }

    @Override
    public boolean doBlend() {
        return true;
    }
}
