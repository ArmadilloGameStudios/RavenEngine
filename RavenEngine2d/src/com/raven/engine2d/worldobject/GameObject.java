package com.raven.engine2d.worldobject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class GameObject<GO extends GameObject, P extends Parentable, C extends GameObject>
        implements com.raven.engine2d.worldobject.Childable<P>, Parentable<C> {

    private static int last_id = 0;
    private static HashMap<Integer, GameObject> gameObjectIDMap = new HashMap<>();

    public static void resetObjectIDs() {
        gameObjectIDMap.clear();
        last_id = 0;
    }

    public static GameObject getGameObjectFromID(int id) {
        return gameObjectIDMap.get(id);
    }

    private boolean visibility = true;
    private int id;
    private boolean mouseHovering = false;
    private List<MouseHandler> clickHandlers = new ArrayList<MouseHandler>();

    public GameObject() {
        // click id
        id = ++last_id;
        gameObjectIDMap.put(id, this);
    }

    public final int getID() {
        return id;
    }

    public void addMouseHandler(MouseHandler c) {
        this.clickHandlers.add(c);
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

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;

        onSetVisibility(visibility);
    }

    protected void onSetVisibility(boolean visibility) {

    }

    public void release() {
        for (C child : getChildren()) {
            child.release();
        }
    }

    public abstract List<GO> getParentGameObjectList();
}
