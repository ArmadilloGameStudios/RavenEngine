package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.GameObject;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

public abstract class UIObject<S extends Scene<?>>
        extends GameObject<UIObject<S>> {

    private final List<UIObject<S>> children = new ArrayList<>();

    private float z = .01f;
    private final S scene;
    private GameObject<?> parent;
    private Layer.Destination destination = Layer.Destination.UI;

    public UIObject(S scene) {
        this.scene = scene;
        scene.addGameObject(this);
    }

    @Override
    public void setParent(GameObject<?> parent) {
        this.parent = parent;
    }

    @Override
    public GameObject<?> getParent() {
        return parent;
    }

    @Override
    public void update(float deltaTime) {

        this.onUpdate(deltaTime);

        for (UIObject<S> c : children) {
            c.update(deltaTime);
        }
    }

    public void onUpdate(float deltaTime) {

    }

    @Override
    public final void addChild(UIObject<S> obj) {
        if (obj.getParent() != this)
            obj.setParent(this);

        if (!children.contains(obj))
            children.add(obj);
    }

    @Override
    public final void needsRedraw() {
        getScene().getLayer(getDestination()).setNeedRedraw(true);
    }

    public final void setDestination(Layer.Destination destination) {
        getScene().getLayer(this.destination).removeChild(this);

        this.destination = destination;

        getScene().getLayer(this.destination).addChild(this);
    }

    @Override
    public final Layer.Destination getDestination() {
        return destination;
    }

    @Override
    public final List<UIObject<S>> getChildren() {
        return children;
    }

    private final List<GameObject<?>> parentList = new ArrayList<>();

    @Override
    public List<GameObject<?>> getParentGameObjectList() {
        parentList.clear();

        if (getParent() instanceof UIObject<?>) {
            parentList.addAll(parent.getParentGameObjectList());
            parentList.add(parent);
        }

        return parentList;
    }

    public abstract Vector2f getPosition();

    private final Vector2f worldPos = new Vector2f();

    public Vector2f getWorldPosition() {
        if (getParent() instanceof UIObject<?>) {
            return getPosition().add(((UIObject<?>) getParent()).getWorldPosition(), worldPos);
        } else {
            return getPosition();
        }
    }

    public S getScene() {
        return scene;
    }

    @Override
    public void draw(LayerShader shader) {

    }

    @Override
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getWorldZ() {
        if (getParent() instanceof UIObject<?>) {
            return getZ() + (((UIObject<?>) getParent()).getWorldZ());
        }
        return getZ();
    }

    private MouseHandler tooltipHandler;
    private String tooltipTitle;
    private String tooltipText;

    public void setToolTipSrc(String src) {
        getScene().getToolTip().getTips().stream()
                .filter(gd -> gd.getString("src").equals(src))
                .findFirst()
                .ifPresent(tip -> {
                    setToolTip(tip.getString("title"), tip.getString("text"));
                });
    }

    public void setToolTip(String title, String text) {
        tooltipTitle = title;
        tooltipText = text;

        if (tooltipHandler == null)
            this.addMouseHandler(tooltipHandler = new MouseHandler() {

                float totalTime = 0f;
                boolean showing = false;

                @Override
                public void handleMouseClick() {

                }

                @Override
                public void handleMouseEnter() {
                    totalTime = 0;
                    showing = false;
                }

                @Override
                public void handleMouseLeave() {
                    scene.hideToolTip();
                    showing = false;
                }

                @Override
                public void handleMouseHover(float delta) {
                    totalTime += delta;

                    if (totalTime > 300f && !showing) {
                        scene.showToolTip(tooltipTitle, tooltipText);
                        showing = true;
                    }
                }
            });
    }

    protected void removeToolTip() {
        this.removeMouseHandler(tooltipHandler);
        tooltipHandler = null;
    }

    public abstract float getX();

    public abstract void setX(float x);

    public abstract float getY();

    public abstract void setY(float y);

    public abstract float getWidth();

    public abstract float getHeight();
}
