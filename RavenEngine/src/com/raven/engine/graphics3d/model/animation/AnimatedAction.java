package com.raven.engine.graphics3d.model.animation;

import java.util.List;

public class AnimatedAction {

    private String name;
    private int[] keyframes;
    private List<Bone> bones;

    public AnimatedAction(String name) {
        this.name = name;
    }
}
