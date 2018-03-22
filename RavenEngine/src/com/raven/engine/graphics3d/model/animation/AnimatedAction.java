package com.raven.engine.graphics3d.model.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimatedAction {

    private String name;
    private int[] keyframes;
    private List<Bone> bones = new ArrayList<>();
    private Bone root;

    public AnimatedAction(String name) {
        this.name = name;
    }

    public void addBone(Bone bone) {
        bones.add(bone);

        if (bone.getParentName() == null)
            root = bone;
    }

    public String getName() {
        return name;
    }

    public void setKeyframes(int[] keyframes) {
        this.keyframes = keyframes;
    }

    public void structureBones() {
        bones.forEach(b -> bones.stream()
                .filter(p -> p.getName().equals(b.getParentName()))
                .findAny()
                .ifPresent(b::setParent));
    }
}
