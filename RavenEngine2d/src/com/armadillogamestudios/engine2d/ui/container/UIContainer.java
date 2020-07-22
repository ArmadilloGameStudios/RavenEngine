package com.armadillogamestudios.engine2d.ui.container;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.UIObject;
import com.armadillogamestudios.engine2d.worldobject.GameObject;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UIContainer<S extends Scene>
        extends UIObject<S, Scene<? extends Game>> {

    public enum Location {
        UPPER_LEFT, UPPER, UPPER_RIGHT,
        LEFT, CENTER, RIGHT,
        BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT
    }

    public enum Layout {
        HORIZONTAL, VERTICAL
    }


    protected float width, height;
    private boolean includeInvisible;
    private Location location;
    private Layout layout;
    private Vector2f position = new Vector2f();

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public UIContainer(S scene, Location location, Layout layout) {
        super(scene);

        this.location = location;
        this.layout = layout;
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

    public void setIncludeInvisible(boolean includeInvisible) {
        this.includeInvisible = includeInvisible;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    public Location getLocation() {
        return location;
    }

    public Layout getLayout() {
        return layout;
    }

    public void pack() {
        final List<UIObject> children;

        if (includeInvisible)
            children = new ArrayList<>(this.getChildren());
        else
            children = this.getChildren().stream().filter(GameObject::isVisible).collect(Collectors.toList());

        height = calcHeight(children);
        width = calcWidth(children);

        // set pos
        switch (getLocation()) {
            case UPPER_LEFT:
                float yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX(0f);
                setY(yOffset - height);
                break;
            case UPPER:
                float xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();
                yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX((xOffset - width) / 2f);
                setY(yOffset - height);
                break;
            case UPPER_RIGHT:
                xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();
                yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX(xOffset - width);
                setY(yOffset - height);
                break;
            case LEFT:
                yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX(0);
                setY((yOffset - height) / 2f);
                break;
            case CENTER: // center
                xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();
                yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX((xOffset - width) / 2f);
                setY((yOffset - height) / 2f);
                break;
            case RIGHT:
                xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();
                yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

                setX(xOffset - width);
                setY((yOffset - height) / 2f);
                break;
            case BOTTOM_LEFT:
                setX(0f);
                setY(0f);
                break;
            case BOTTOM:
                xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();

                setX((xOffset - width) / 2f);
                setY(0f);
                break;
            case BOTTOM_RIGHT:
                xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();

                setX(xOffset - width);
                setY(0f);
                break;
        }

        switch (getLayout()) {
            case VERTICAL:
                float yOffset = 0f;

                for (int i = children.size() - 1; i >= 0; i--) {
                    children.get(i).setY(yOffset);
                    yOffset += children.get(i).getHeight();
                }
                break;
            case HORIZONTAL:
                float xOffset = 0f;

                for (UIObject<?, ?> child : children) {
                    child.setX(xOffset);
                    xOffset += child.getWidth();
                }
                break;

        }
    }

    private float calcHeight(List<UIObject> children) {
        float height = 0f;

        switch (getLayout()) {
            case VERTICAL:
                for (UIObject<?, ?> obj : children) {
                    height += obj.getHeight();
                }
                break;
            case HORIZONTAL:
                for (UIObject<?, ?> obj : children) {
                    height = Math.max(obj.getHeight(), height);
                }
                break;
        }

        return height;
    }

    private float calcWidth(List<UIObject> children) {
        float width = 0f;

        switch (getLayout()) {
            case VERTICAL:
                for (UIObject obj : children) {
                    width = Math.max(obj.getWidth(), width);
                }
                break;
            case HORIZONTAL:
                for (UIObject obj : children) {
                    width += obj.getWidth();
                }
                break;
        }

        return width;
    }
}
