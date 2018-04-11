package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationAction;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.Parentable;

import java.util.ArrayList;
import java.util.List;

public abstract class UIObject<S extends Scene, P extends Parentable<UIObject>>
        extends GameObject<UIObject, P, UIObject> {

    private List<UIObject> children = new ArrayList<>();

    private S scene;
    private P parent;
    private boolean parentIsHUDObject = false;

    public UIObject(S scene) {
        this.scene = scene;
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
        obj.parentIsHUDObject = true;

        obj.setParent(this);
        children.add(obj);
    }

    @Override
    public List<UIObject> getChildren() {
        return children;
    }

    private List<UIObject> parentList = new ArrayList();

    @Override
    public List<UIObject> getParentGameObjectList() {
        parentList.clear();

        if (parentIsHUDObject) {
            parentList.addAll(((UIObject) parent).getParentGameObjectList());
            parentList.add((UIObject) parent);
        }

        return parentList;
    }

    public S getScene() {
        return scene;
    }

    public void draw(MainShader shader) {
        for (UIObject o : this.getChildren()) {
            if (o.getVisibility())
                o.draw(shader);
        }
    }

    public abstract int getStyle();

    public abstract float getHeight();

    public abstract float getWidth();

    public abstract float getYOffset();

    public abstract void setYOffset(float y);

    public abstract float getXOffset();

    public abstract void setXOffset(float x);

    public abstract Vector4f getColor();

    public abstract boolean doBlend();
}
