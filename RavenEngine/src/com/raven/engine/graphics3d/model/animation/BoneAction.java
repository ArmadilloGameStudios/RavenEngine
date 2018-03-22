package com.raven.engine.graphics3d.model.animation;

import com.raven.engine.util.Quaternion;
import com.raven.engine.util.Vector3f;

public class BoneAction {

    private final Bone bone;
    private int keyframes;

    private Vector3f[] location;
    private Quaternion[] rotation;
    private Vector3f[] scale;
    private Vector3f[] vector;

    public BoneAction(Bone bone) {
        this.bone = bone;
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
}
