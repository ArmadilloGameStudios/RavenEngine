package com.raven.engine2d.ui;

import com.raven.engine2d.Game;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.GameObject;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UIContainer<S extends Scene>
        extends UIObject<S, Scene<? extends Game>> {

    public static final int CENTER = 0, BOTTOM_LEFT = 1, BOTTOM = 2, BOTTOM_RIGHT = 3, RIGHT = 4;

    protected float width, height;
    private Vector2f position = new Vector2f();

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


    @Override
    public final float getY() {
        return position.y;
    }

    @Override
    public final void setY(float y) {
        position.y = y;
    }

    @Override
    public final float getX() {
        return position.x;
    }

    @Override
    public final void setX(float x) {
        position.x = x;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    public void pack() {
        final List<UIObject> children = this.getChildren().stream().filter(GameObject::isVisible).collect(Collectors.toList());

        switch (getStyle()) {
            case BOTTOM:
                width = 0;
                height = 0f;

                for (UIObject obj : children) {
                    height = Math.max(obj.getHeight(), height);
                    width += obj.getWidth();
                }

                // Get Offset
                float offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    obj.setX(offset);

                    offset += obj.getWidth() * 2f;
                }
                break;
            case BOTTOM_LEFT:
                width = 0;
                height = 0f;

                for (UIObject obj : children) {
                    height = Math.max(obj.getHeight(), height);
                    width += obj.getWidth();
                }

                // Get Offset
                offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    obj.setX(offset);

                    offset += obj.getWidth() * 2f;
                }
                break;
            case BOTTOM_RIGHT:
                width = 0;
                height = 0f;

                for (UIObject obj : children) {
                    height = Math.max(obj.getHeight(), height);
                    width += obj.getWidth();
                }

                // Get Offset
                offset = GameProperties.getScreenWidth() * 2f / GameProperties.getScaling();

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    offset -= obj.getWidth() * 2f;

                    obj.setX(offset);
                }
                break;
            case RIGHT:
                width = 0f;
                height = 0;

                for (UIObject obj : children) {
                    width = Math.max(obj.getWidth(), width);
                    height += obj.getHeight();
                }

                // Get Offset
                offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    offset += obj.getHeight() * 2f;

                    obj.setY(height - offset + GameProperties.getScreenHeight() / GameProperties.getScaling());
                    obj.setX(GameProperties.getScreenWidth() / GameProperties.getScaling() * 2f - obj.getWidth() * 2f);
                }
                break;
            case CENTER: // center
            default:
                width = 0f;
                height = 0;

                for (UIObject obj : children) {
                    width = Math.max(obj.getWidth(), width);
                    height += obj.getHeight();
                }

                // Get Offset
                offset = 0f;

                for (int i = 0; i < children.size(); i++) {
                    UIObject obj = children.get(i);

                    offset += obj.getHeight() * 2f;

                    obj.setY(height - offset + GameProperties.getScreenHeight() / GameProperties.getScaling());
                    obj.setX(GameProperties.getScreenWidth() / GameProperties.getScaling() - obj.getWidth());


//                    offset += obj.getHeight() * .5f;
                }
                break;
        }
    }
}
