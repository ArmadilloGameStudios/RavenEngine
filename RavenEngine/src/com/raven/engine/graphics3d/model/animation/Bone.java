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

    public void setKeyframesLength(int keyframesLength) {
        this.keyframes = keyframesLength;
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

    private Quaternion qout = new Quaternion();
    private Vector3f vout = new Vector3f();
    private Vector3f tempVec = new Vector3f();
    private Matrix4f tempMat = new Matrix4f();
    private Matrix4f tempMat2 = new Matrix4f();

    // TODO check if already calculated
    public Matrix4f matrix(int keyframe, float mix) {

        Vector3f va = head[keyframe], vb;
        Quaternion qa = rotation[keyframe], qb;

        if (keyframe != this.keyframes - 1) {
            vb = head[keyframe + 1];
            qb = rotation[keyframe + 1];
        } else {
            vb = head[0];
            qb = rotation[0];
        }

        tempMat2.identity();

        // translate
        Vector3f.lerp(va, vb, mix, vout);
        tempMat2.translate(vout, tempMat);

        // rotate
        Quaternion.lerp(qa, qb, mix, qout);
        qout.toMatrix(tempMat2);
        tempMat.multiply(tempMat2, outMatrix);

        // translate back

        outMatrix.translate(vout.negate(tempVec), tempMat2);


        // do parent
        if (parent != null)
            tempMat2.multiply(parent.matrix(keyframe, mix), outMatrix);

        return outMatrix;
    }


    public void toBuffer(FloatBuffer aBuffer, int keyframe, float mix) {
        matrix(keyframe, mix).toBuffer(aBuffer);
    }
}
