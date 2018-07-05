package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.Parentable;

import java.util.ArrayList;
import java.util.List;

public abstract class UIObject<S extends Scene, P extends Parentable<? extends GameObject>>
        extends GameObject<UIObject, P, UIObject> {

    private List<UIObject> children = new ArrayList<>();

    private S scene;
    private P parent;
    private boolean parentIsUIObject = false;

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

    }

    @Override
    public void addChild(UIObject obj) {
        obj.parentIsUIObject = true;

        obj.setParent(this);
        children.add(obj);
    }


    @Override
    public final Layer.Destination getDestination() {
        return Layer.Destination.UI;
    }

    @Override
    public List<UIObject> getChildren() {
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
        }
        return worldPos;
    }

    public S getScene() {
        return scene;
    }

    public void draw(MainShader shader) {

    }

    @Override
    public float getZ() {
        return 0.0f;
    }

    public abstract int getStyle();

    public abstract float getHeight();

    public abstract float getWidth();

    public abstract float getY();

    public abstract void setY(float y);

    public abstract float getX();

    public abstract void setX(float x);
}
