package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.MouseHandler;
import com.raven.engine2d.worldobject.Parentable;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public abstract class UIObject<S extends Scene, P extends Parentable<? extends GameObject>>
        extends GameObject<UIObject, P, UIObject> {

    private List<UIObject> children = new ArrayList<>();

    private float z = .01f;
    private S scene;
    private P parent;
    private boolean parentIsUIObject = false;
    private Layer.Destination destination = Layer.Destination.UI;

    public UIObject(S scene) {
        this.scene = scene;
        scene.addGameObject(this);
    }

    @Override
    public void setParent(P parent) {
        this.parent = parent;
    }

    @Override
    public P getParent() {
        return parent;
    }

    @Override
    public void update(float deltaTime) {

        this.onUpdate(deltaTime);

        for (UIObject c : children) {
            c.update(deltaTime);
        }
    }

    public void onUpdate(float deltaTime) {

    }

    @Override
    public final void addChild(UIObject obj) {
        obj.parentIsUIObject = true;

        if (obj.getParent() != this)
            obj.setParent(this);

        if (!children.contains(obj))
            children.add(obj);
        else
            System.out.println("dup");
    }

    public void removeChild(UIObject obj) {
        children.remove(obj);
    }

    public final void setDestination(Layer.Destination destination) {
        this.destination = destination;
    }

    @Override
    public final Layer.Destination getDestination() {
        return destination;
    }

    @Override
    public final List<UIObject> getChildren() {
        return children;
    }

    private List<UIObject> parentList = new ArrayList();

    @Override
    public List<UIObject> getParentGameObjectList() {
        parentList.clear();

        if (parentIsUIObject) {
            parentList.addAll(((UIObject) parent).getParentGameObjectList());
            parentList.add((UIObject) parent);
        }

        return parentList;
    }

    public abstract Vector2f getPosition();

    private Vector2f worldPos = new Vector2f();

    public Vector2f getWorldPosition() {
        if (this.parentIsUIObject) {
            return getPosition().add(((UIObject) getParent()).getWorldPosition(), worldPos);
        } else {
            return getPosition();
        }
    }

    public S getScene() {
        return scene;
    }

    public void draw(MainShader shader) {

    }

    @Override
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getWorldZ() {
        if (getParent() instanceof UIObject) {
            return getZ() + (((UIObject) getParent()).getWorldZ());
        }
        return getZ();
    }

    private MouseHandler tooltipHandler;
    private String tooltipSrc;

    public void setToolTip(String tooltip) {
        this.tooltipSrc = tooltip;

        if (tooltipHandler == null)
            this.addMouseHandler(tooltipHandler = new MouseHandler() {
                @Override
                public void handleMouseClick() {

                }

                @Override
                public void handleMouseEnter() {
                    scene.showToolTip(tooltipSrc);
                }

                @Override
                public void handleMouseLeave() {
                    scene.hideToolTip();
                }

                @Override
                public void handleMouseHover(float delta) {

                }
            });
    }

    public abstract int getStyle();

    public abstract float getX();

    public abstract void setX(float x);

    public abstract float getY();

    public abstract void setY(float y);

    public abstract float getWidth();

    public abstract float getHeight();
}
