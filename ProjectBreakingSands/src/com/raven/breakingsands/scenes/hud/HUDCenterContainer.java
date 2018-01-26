package com.raven.breakingsands.scenes.hud;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.HUDContainer;

public class HUDCenterContainer extends HUDContainer {

    public HUDCenterContainer(Scene scene) {
        super(scene);
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
    public float getXOffset() {
        return 0;
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
    public void addChild(Childable obj) {

    }
}
