package com.raven.engine.graphics3d.model.animation;

import java.nio.FloatBuffer;
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
        // TODO calculate bone matrices?
        bones.forEach(b -> bones.stream()
                .filter(p -> p.getName().equals(b.getParentName()))
                .findAny()
                .ifPresent(b::setParent));
    }

    public List<Bone> getBones() {
        return bones;
    }


    float time = 0;

    public void toBuffer(FloatBuffer aBuffer) {

        time += .1f;

        float frame = time % 120;

        int i = 0;
        for (; i < keyframes.length; i++)
            if (frame < keyframes[i])
                break;

        final int keyframe = i - 1;

        System.out.println(keyframe);

        bones.forEach(bone -> {
            bone.toBuffer(aBuffer, keyframe, 0);
        });
    }
}
