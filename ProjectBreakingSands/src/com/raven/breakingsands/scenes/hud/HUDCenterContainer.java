package com.raven.breakingsands.scenes.hud;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.HUDContainer;

public class HUDCenterContainer extends HUDContainer {

    private float x, y;

    public HUDCenterContainer(Scene scene) {
        super(scene);
    }

    @Override
    public float getBorder() {
        return 15f;
    }

    @Override
    public int getStyle() {
        return 0;
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
    public float getYOffset() {
        return 500f;
    }

    @Override
    public void setYOffset(float y) {
        this.y = y;
    }

    @Override
    public float getXOffset() {
        return 0;
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
