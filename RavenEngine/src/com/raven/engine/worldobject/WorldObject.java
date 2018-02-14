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

    private List<WorldObject> parentList = new ArrayList<>();
    private S scene;

    private float scale = 1f, rotation = 0f;
    private Vector3f position = new Vector3f();
    private Matrix4f matrix = new Matrix4f();

    private Highlight highlight = new Highlight();

    private boolean visible = true;

    private List<C> children = new ArrayList<>();
    private ModelData model;
    private long timeOffset = 0;

    P parent;
    boolean parentIsWorldObject;

    public WorldObject(S scene, String modelsrc) {
        this(scene, GameEngine.getEngine().getModelData(modelsrc));
    }

    public WorldObject(S scene, ModelData model) {
        // model
        this.scene = scene;
        this.model = model;

        resolveMatrix();
    }
    public float getZ() {
        return position.z;
    }

    public void setZ(float z) {
        position.z = z;
        resolveMatrix();
    }

    public void moveZ(float z) {
        setZ(getZ() + z);
    }

    public float getX() {
        return position.x;
    }

    public void setX(float x) {
        position.x = x;
        resolveMatrix();
    }

    public void moveX(float x) {
        setX(getX() + x);
    }

    public float getY() {
        return position.y;
    }

    public void setY(float y) {
        position.y = y;
        resolveMatrix();
    }

    public void moveY(float y) {
        setY(getY() + y);
    }

    // TODO
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        resolveMatrix();
    }

    public void setPosition(Vector3f position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
        resolveMatrix();
    }

    public void move(Vector3f amount) {
        setPosition(getPosition().add(amount, getPosition()));
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

    private Matrix4f tempMat = new Matrix4f();
    private void resolveMatrix() {
        matrix.identity();
        tempMat.identity();

        tempMat.translate(position, matrix);
        matrix.rotate(rotation, 0f, 1f, 0f, tempMat);
        tempMat.scale(scale, scale, scale, matrix);
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

    private Matrix4f drawMat = new Matrix4f();
    public void draw4(Matrix4f parentMatrix) {
        GameEngine.getEngine().getWindow().getWorldShader().setWorldObjectID(getID());

        Shader.setModelMatrix(parentMatrix.multiply(getModelMatrix(), drawMat));
        model.getModelReference().draw();

        for (C child : children) {
//            child.draw4(); ??
            child.draw4(drawMat);
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
