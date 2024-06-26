package com.raven.engine.worldobject;

import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;

import java.util.List;

public abstract class HUDContainer<S extends Scene>
        extends HUDObject<S, Layer<HUDObject>> {

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

    public HUDContainer(S scene) {
        super(scene);
    }

    public abstract float getBorder();

    @Override
    public void addChild(HUDObject child) {
        super.addChild(child);
    }

    public void pack() {
        switch (getStyle()) {
            case BOTTOM: // bottom
                width = getBorder();
                height = 0f;

                List<HUDObject> children = this.getChildren();

                for (HUDObject obj : children) {
                    height = Math.max(obj.getHeight() + getBorder() * 2f, height);
                    width += getBorder() + obj.getWidth();
                }

                // Get Offset
                float offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    HUDObject obj = children.get(i);

                    obj.setXOffset(getBorder() * 2f + getBorder() * i * 2f - width + obj.getWidth() + offset);

                    offset += obj.getWidth() * 2f;
                }
                break;
            case CENTER: // center
            default:
                width = 0f;
                height = getBorder();

                children = this.getChildren();

                for (HUDObject obj : children) {
                    width = Math.max(obj.getWidth() + getBorder() * 2f, width);
                    height += getBorder() + obj.getHeight();
                }

                // Get Offset
                offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    HUDObject obj = children.get(i);

                    offset += obj.getHeight() * .5f + getBorder();

                    obj.setYOffset(height * .5f - offset);

                    offset += obj.getHeight() * .5f;

                }
                break;
        }
    }
}
