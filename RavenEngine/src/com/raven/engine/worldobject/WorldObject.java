package com.raven.engine.worldobject;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.shader.Shader;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

public abstract class WorldObject<
            S extends Scene,
            P extends Parentable<WorldObject>,
            C extends WorldObject>
        extends GameObject<WorldObject, P, C> {

    private List<WorldObject> parentList = new ArrayList();
    private S scene;

    private float x, y, z, scale = 1f, rotation = 0f;
    private Matrix4f matrix = new Matrix4f();

    private Highlight highlight = new Highlight();

    private boolean visible = true;

    private List<C> children = new ArrayList<>();
    private ModelData model;
    private long timeOffset = 0;
    private List<TextObject> textObjects = new ArrayList<TextObject>();

    P parent;
    boolean parentIsWorldObject;

    public WorldObject(S scene, String modelsrc) {
        this(scene, GameEngine.getEngine().getModelData(modelsrc));
    }

    public WorldObject(S scene, ModelData model) {
        // model
        this.scene = scene;
        this.model = model;

        // pos
        this.x = x;
        this.y = y;
        this.z = 0;

        resolveMatrix();
    }
    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
        resolveMatrix();
    }

    public void moveZ(float z) {
        setZ(getZ() + z);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        resolveMatrix();
    }

    public void moveX(float x) {
        setX(getX() + x);
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        resolveMatrix();
    }

    public void moveY(float y) {
        setY(getY() + y);
    }

    public Vector3f getPosition() {
        return new Vector3f(x, y, z);
    }

    public void setPosition(Vector3f position) {
        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        resolveMatrix();
    }

    public void move(Vector3f amount) {
        setPosition(getPosition().add(amount));
    }

    public S getScene() {
        return scene;
    }

    public void setScale(float scale) {
        this.scale = scale;
        resolveMatrix();
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        resolveMatrix();
    }

    private void resolveMatrix() {
        matrix = matrix.identity()
                .translate(x, y, z)
                .rotate(rotation, 0f, 1f, 0f)
                .scale(scale, scale, scale);
    }

    public Matrix4f getModelMatrix() {
        return matrix;
    }

    public void setHighlight(Highlight h) {
        highlight = h;
    }

    public Highlight getHighlight() {
        return highlight;
    }

    public boolean getVisibility() {
        return this.visible;
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
    }

    public void draw4ms() {
        GameEngine.getEngine().getWindow().getWorldMSShader().setWorldObjectID(getID());

        model.getModelReference().draw();
    }

    public void draw4() {
        GameEngine.getEngine().getWindow().getWorldShader().setWorldObjectID(getID());

        Shader.setModelMatrix(getModelMatrix());
        model.getModelReference().draw();

        for (C child : children) {
            child.draw4(getModelMatrix());
        }
    }

    // TODO fix memory
    public void draw4(Matrix4f parentMatrix) {
        GameEngine.getEngine().getWindow().getWorldShader().setWorldObjectID(getID());

        Shader.setModelMatrix(parentMatrix.multiply(getModelMatrix()));
        model.getModelReference().draw();

        for (WorldObject child : children) {
            child.draw4();
        }
    }

    public void draw2() {
        model.getModelReference().draw();
    }

    public void setParent(P parent) {
        this.parent = parent;
    }

    public P getParent() {
        return parent;
    }

    @Override
    public List<WorldObject> getParentGameObjectList() {
        parentList.clear();

        if (parentIsWorldObject) {
            parentList.addAll(((WorldObject) parent).getParentGameObjectList());
            parentList.add((WorldObject) parent);
        }

        return parentList;
    }

    @Override
    public List<C> getChildren() {
        return children;
    }

    @Override
    public void addChild(C child) {
        child.parentIsWorldObject = true;
        child.parent = this;

        children.add(child);
    }

    public void removeAllChildren() {
        children.clear();
    }

    public void removeChild(WorldObject child) {
        children.remove(child);
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
