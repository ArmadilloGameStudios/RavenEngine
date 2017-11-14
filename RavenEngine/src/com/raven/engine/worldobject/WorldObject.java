package com.raven.engine.worldobject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.ModelReference;
import com.raven.engine.util.Matrix4f;

public abstract class WorldObject implements Parentable {
    private static int last_id = 0;
    private static HashMap<Integer, WorldObject> worldObjectIDMap = new HashMap<Integer, WorldObject>();

    public static void resetObjectIDs() {
        worldObjectIDMap.clear();
        last_id = 0;
    }

    public static WorldObject getWorldObjectFromID(int id) {
        return worldObjectIDMap.get(id);
    }

    private int id;
    private float x, y, z, scale = 1f, rotation = 0f;
    private Matrix4f matrix;
    private boolean visible = true;
    private List<WorldObject> children = new CopyOnWriteArrayList<WorldObject>();
    private ModelData model;
    private String animationstate = "idle";
    private boolean mousehovering = false;
    private List<MouseHandler> clickHandlers = new ArrayList<MouseHandler>();
    private long timeOffset = 0;

    private List<TextObject> textObjects = new ArrayList<TextObject>();

    private Parentable parent;
    private boolean parentIsWorldObject;

    public WorldObject(Parentable parent, String modelsrc) {
        this(parent, GameEngine.getEngine().getModelData(GameEngine.getEngine().getGame().getMainDirectory() + File.separator + modelsrc));
    }

    public WorldObject(Parentable parent, ModelData model) {
        this.parent = parent;

        // model
        this.model = model;

        // click id
        id = ++last_id;
        worldObjectIDMap.put(id, this);

        // pos
        this.x = x;
        this.y = y;
        this.z = 0;

        resolveMatrix();
    }

    public String getAnimationState() {
        return animationstate;
    }

    public int setAnimationState(String animationstate) {
        // TODO
        return 0;
    }

    public int getAnimationTime(String animationstate) {
        // TODO
        return 0;
    }

    @Override
    public float getGlobalZ() {
        return this.getZ() + parent.getGlobalZ();
    }

    public float getZ() {
        return x;
    }

    public void setZ(float z) {
        this.z = z;
        resolveMatrix();
    }

    @Override
    public float getGlobalX() {
        return this.getX() + parent.getGlobalX();
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        resolveMatrix();
    }

    @Override
    public float getGlobalY() {
        return this.getY() + parent.getGlobalY();
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        resolveMatrix();
    }

    public void setScale(float scale) {
        this.scale = scale;
        resolveMatrix();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        resolveMatrix();
    }

    private void resolveMatrix() {
        matrix = Matrix4f.translate(x, y, z)
                .multiply(Matrix4f.rotate(rotation, 0f, 1f, 0f))
                .multiply(Matrix4f.scale(scale, scale, scale));
    }

    public boolean getVisibility() {
        return this.visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public void addText(TextObject text) {
        this.textObjects.add(text);
    }

    public void removeText(TextObject text) {
        this.textObjects.remove(text);
    }

    public void draw() {
        GameEngine.getEngine().getWindow().getWorldShader().setModelMatrix(matrix);
        GameEngine.getEngine().getWindow().getWorldShader().setWorldObjectID(id);

        model.getModelReference().draw();
    }

    public Parentable getParent() {
        return parent;
    }

    public boolean isParentWorldObject() {
        return parentIsWorldObject;
    }

    public List<WorldObject> getParentWorldObjectList() {
        List<WorldObject> list;

        if (parentIsWorldObject) {
            list = ((WorldObject) parent).getParentWorldObjectList();
            list.add((WorldObject) parent);
        } else {
            list = new ArrayList<WorldObject>();
        }

        return list;
    }

    public void addChild(WorldObject child) {
        children.add(child);
    }

    public void removeAllChildren() {
        children.clear();
    }

    public void removeChild(WorldObject child) {
        children.remove(child);
    }

    public void addMouseHandler(MouseHandler c) {
        this.clickHandlers.add(c);
    }

    public boolean isMouseHovering() {
        return mousehovering;
    }

    final public void checkMouseMovement(boolean hovering) {
        if (!isMouseHovering() && hovering) {
            onMouseEnter();
        } else if (isMouseHovering() && !hovering) {
            onMouseLeave();
        } else if (hovering) {
            onMouseMove();
        }

        mousehovering = hovering;
    }

    final public void onMouseEnter() {
        for (MouseHandler c : clickHandlers) c.onMouseEnter();
    }

    final public void onMouseMove() {
        for (MouseHandler c : clickHandlers) c.onMouseMove();
    }

    final public void onMouseLeave() {
        for (MouseHandler c : clickHandlers) c.onMouseLeave();
    }

    final public void onMouseClick() {
        for (MouseHandler c : clickHandlers) c.onMouseClick();
    }

    final public void update(float deltaTime) {
        this.onUpdate(deltaTime);

        for (WorldObject c : children) {
            c.update(deltaTime);
        }
    }

    public void onUpdate(float deltaTime) {

    }
}
