package com.raven.engine.worldobject;

import com.raven.engine.Game;
import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.shader.HUDMSShader;
import com.raven.engine.graphics3d.shader.HUDShader;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;

import java.util.ArrayList;
import java.util.List;

public abstract class HUDObject<S extends Scene, P extends Parentable<HUDObject>>
        extends GameObject<HUDObject, P, HUDObject> {

    private List<HUDObject> children = new ArrayList<>();

    private S scene;
    private P parent;
    private boolean parentIsHUDObject = false;

    public HUDObject(S scene) {
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
    public void update(float delta) {

    }

    @Override
    public void addChild(HUDObject obj) {
        obj.parentIsHUDObject = true;

        obj.setParent(this);
        children.add(obj);
    }

    @Override
    public List<HUDObject> getChildren() {
        return children;
    }

    private List<HUDObject> parentList = new ArrayList();
    @Override
    public List<HUDObject> getParentGameObjectList() {
        parentList.clear();

        if (parentIsHUDObject) {
            parentList.addAll(((HUDObject) parent).getParentGameObjectList());
            parentList.add((HUDObject) parent);
        }

        return parentList;
    }

    public S getScene() {
        return scene;
    }

    public void draw(GameWindow window, HUDShader shader) {
        shader.setProperties(this);
        window.drawQuad();

        for (HUDObject o : this.getChildren()) {
            if (o.getVisibility())
                o.draw(window, shader);
        }
    }

    public void draw(GameWindow window, HUDMSShader shader) {
        shader.setProperties(this);
        window.drawQuad();

        for (HUDObject o : this.getChildren()) {
            if (o.getVisibility())
                o.draw(window, shader);
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
