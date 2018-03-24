package com.raven.engine.graphics3d.model.animation;

import com.raven.engine.util.math.Matrix4f;
import com.raven.engine.util.math.Quaternion;
import com.raven.engine.util.math.Vector3f;

import java.nio.FloatBuffer;

public class Bone {

    private final String name;

    private Bone parent;
    private String parentName;

    private int keyframes;

    private Vector3f[] location;
    private Quaternion[] rotation;
    private Vector3f[] scale;
    private Vector3f[] vector;
    private Vector3f[] head;
    private Vector3f[] tail;

    private Matrix4f outMatrix = new Matrix4f();
    private Matrix4f catMat = new Matrix4f();


    public Bone(String name) {
        this.name = name;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setParent(Bone parent) {
        this.parent = parent;
    }

    public void setHead(Vector3f[] head) {
        this.head = head;
    }

    public void setTail(Vector3f[] tail) {
        this.tail = tail;
    }

    public void setLocation(Vector3f[] location) {
        this.location = location;
    }

    public void setRotation(Quaternion[] rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3f[] scale) {
        this.scale = scale;
    }

    public void setVector(Vector3f[] vector) {
        this.vector = vector;
    }

    public String getParentName() {
        return parentName;
    }

    public String getName() {
        return name;
    }

    float time = 0;

    public void toBuffer(FloatBuffer aBuffer, int keyframe, float mix) {

        time += .001f;
        rotation[keyframe].toMatrix(outMatrix);

//        catMat.rotate(rotation[0].x * 50000f * (float)Math.sin(time), .2f, .3f, .3f, outMatrix);
        outMatrix.toBuffer(aBuffer);
    }
}
