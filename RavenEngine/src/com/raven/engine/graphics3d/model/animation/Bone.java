package com.raven.engine.graphics3d.model.animation;

import com.raven.engine.util.Vector3f;

import java.util.List;

public class Bone {

    private final String name;
    private Vector3f head, tail;
    private Bone parent;
    private List<BoneAction> boneActions;
    private String parentName;


    public Bone(String name) {
        this.name = name;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setHead(Vector3f head) {
        this.head = head;
    }

    public void setTail(Vector3f tail) {
        this.tail = tail;
    }
}
