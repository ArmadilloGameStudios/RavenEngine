package com.raven.engine.scene.light;

import com.raven.engine.util.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by cookedbird on 12/6/17.
 */
public class AmbientLight extends Light {

    public float intensity = 1f;

    public AmbientLight() {
        this.intensity = .2f;
    }

    public AmbientLight(float intensity) {
        this.intensity = intensity;
    }

    @Override
    public void toFloatBuffer(FloatBuffer buffer) {

    }

    @Override
    public int getLightType() {
        return Light.AMBIANT;
    }
}
