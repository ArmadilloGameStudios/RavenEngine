package com.raven.breakingsands.scenes;

import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDObject;

public class HUDBottomContainer extends HUDContainer<BattleScene> {

    private Vector4f color = new Vector4f(.25f,.25f,.25f,.5f);

    public HUDBottomContainer(BattleScene scene) {
        super(scene);

        HUDObject<BattleScene, HUDBottomContainer> obj = new HUDObject<BattleScene, HUDBottomContainer>(scene) {

            private Vector4f color = new Vector4f(.25f,.25f,.25f,1f);

            @Override
            public int getStyle() {
                return getParent().getStyle();
            }

            @Override
            public float getHeight() {
                return 85f;
            }

            @Override
            public float getWidth() {
                return 85f;
            }

            @Override
            public float getXOffset() {
                return 0f;
            }

            @Override
            public float getYOffset() {
                return getParent().getHeight();
            }

            @Override
            public Vector4f getColor() {
                return color;
            }

            @Override
            public boolean doBlend() {
                return false;
            }
        };

        addChild(obj);
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
