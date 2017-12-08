package com.raven.engine.scene.light;

import com.raven.engine.util.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by cookedbird on 11/30/17.
 */
public abstract class Light {
    public final static int AMBIANT = 0, DIRECTIONAL = 1;

    public abstract void toFloatBuffer(FloatBuffer buffer);

    public abstract int getLightType();
}
