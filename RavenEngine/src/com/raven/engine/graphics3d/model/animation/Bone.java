package com.raven.engine.graphics3d.model.animation;

import com.raven.engine.util.Quaternion;
import com.raven.engine.util.Vector3f;

import java.util.List;

public class Bone {

    private final String name;
    private Vector3f head, tail;

    private Bone parent;
    private String parentName;

    private int keyframes;

    private Vector3f[] location;
    private Quaternion[] rotation;
    private Vector3f[] scale;
    private Vector3f[] vector;


    public Bone(String name) {
        this.name = name;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setParent(Bone parent) {
        this.parent = parent;
    }

    public void setHead(Vector3f head) {
        this.head = head;
    }

    public void setTail(Vector3f tail) {
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
}
