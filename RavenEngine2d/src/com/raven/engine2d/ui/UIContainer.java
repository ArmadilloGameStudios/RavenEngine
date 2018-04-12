package com.raven.engine2d.ui;

import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.scene.Scene;

import java.util.List;

public abstract class UIContainer<S extends Scene>
        extends UIObject<S, Layer<UIObject>> {

    public static final int CENTER = 0, BOTTOM = 1;

    protected float width, height;

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public UIContainer(S scene) {
        super(scene);
    }

    @Override
    public void addChild(UIObject child) {
        super.addChild(child);
    }

    public void pack() {
        switch (getStyle()) {
            case BOTTOM: // bottom
                width = 0;
                height = 0f;

                List<UIObject> children = this.getChildren();

                for (UIObject obj : children) {
                    height = Math.max(obj.getHeight(), height);
                    width += obj.getWidth();
                }

                // Get Offset
                float offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    obj.setXOffset(-width + obj.getWidth() + offset);

                    offset += obj.getWidth() * 2f;
                }
                break;
            case CENTER: // center
            default:
                width = 0f;
                height = 0;

                children = this.getChildren();

                for (UIObject obj : children) {
                    width = Math.max(obj.getWidth(), width);
                    height += obj.getHeight();
                }

                // Get Offset
                offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    offset += obj.getHeight() * .5f;

                    obj.setYOffset(height * .5f - offset);

                    offset += obj.getHeight() * .5f;

                }
                break;
        }
    }
}
