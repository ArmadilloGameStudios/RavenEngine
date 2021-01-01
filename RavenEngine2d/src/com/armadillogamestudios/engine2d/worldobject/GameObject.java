package com.armadillogamestudios.engine2d.worldobject;

import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public abstract class GameObject<C extends GameObject<?>> {

    private static int last_id = 0;
    private static final HashMap<Integer, GameObject<?>> gameObjectIDMap = new HashMap<>();

    public static void resetObjectIDs() {
        gameObjectIDMap.clear();
        last_id = 0;
    }

    public static GameObject<?> getGameObjectFromID(int id) {
        return gameObjectIDMap.get(id);
    }

    private boolean visibility = true;
    private int id;

    private Highlight highlight;
    private float fade = 1f;
    private boolean mouseHovering = false;
    private final List<MouseHandler> clickHandlers = new ArrayList<MouseHandler>();

    public GameObject() {
        // click id
        newID();
    }

    public final int getID() {
        return id;
    }

    public final void clearID() {
        gameObjectIDMap.remove(this.id);
        this.id = 0;
    }

    public final void newID() {
        id = last_id += 20;
        gameObjectIDMap.put(id, this);
    }

    public abstract Layer.Destination getDestination();

    public abstract float getZ();

    public final float getFade() {
        return fade;
    }

    public final void setFade(float fade) {
        this.fade = fade;

        needsRedraw();
    }

    public void addMouseHandler(MouseHandler c) {
        this.clickHandlers.add(c);
    }

    public void removeMouseHandler(MouseHandler c) {
        this.clickHandlers.remove(c);
    }

    public boolean isMouseHovering() {
        return mouseHovering;
    }

    final public void checkMouseMovement(boolean hovering, float delta) {
        if (!isMouseHovering() && hovering) {
            mouseHovering = hovering;
            onMouseEnter();
        } else if (isMouseHovering() && !hovering) {
            mouseHovering = hovering;
            onMouseLeave();
        } else if (hovering) {
            onMouseHover(delta);
        }

        mouseHovering = hovering;
    }

    final public void onMouseEnter() {
        for (MouseHandler c : clickHandlers) c.handleMouseEnter();
    }

    final public void onMouseHover(float delta) {
        for (MouseHandler c : clickHandlers) c.handleMouseHover(delta);
    }

    final public void onMouseLeave() {
        for (MouseHandler c : clickHandlers) c.handleMouseLeave();
    }

    final public void onMouseClick() {
        for (MouseHandler c : clickHandlers) c.handleMouseClick();
    }

    public abstract void needsRedraw();

    public abstract void draw(LayerShader shader);

    public boolean isVisible() {
        return visibility && (getParent() == null || getParent().isVisible());
    }

    public void setVisibility(boolean visibility) {
        needsRedraw();
        this.visibility = visibility;
    }

    public void release() {
        for (C child : getChildren()) {
            child.release();
        }
    }

    public void setHighlight(Highlight h) {
        highlight = h;

        needsRedraw();
    }

    public Highlight getHighlight() {
        if (highlight == null && getParent() != null) {
            return getParent().getHighlight();
        }

        return highlight;
    }

    public abstract List<GameObject<?>> getParentGameObjectList();
    public abstract GameObject<?> getParent();
    public abstract void setParent(GameObject<?> parent);
    public abstract void update(float delta);
    public abstract void addChild(C obj);
    public abstract List<C> getChildren();
}
