package com.raven.engine.scene.light;

import com.raven.engine.graphics3d.shader.ShadowShader;
import com.raven.engine.util.math.Matrix4f;
import com.raven.engine.util.math.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by cookedbird on 11/30/17.
 */
public class GlobalDirectionalLight extends Light {
    public Vector3f origin = new Vector3f();
    public float size = 20f, height = 4f;

    private Matrix4f shadowViewMatrix = new Matrix4f();
    private Matrix4f shadowProjectionMatrix = new Matrix4f();
    public Vector3f color = new Vector3f();
    public float intensity = 1f;
    private Vector3f direction = new Vector3f();
    public float length = 1f;
    private Vector3f ambient = new Vector3f(.1f, .1f, .1f);
    public float shadowTransparency = 1.0f;


    private Vector3f tempVec = new Vector3f();

    public GlobalDirectionalLight() {
        this(new Vector3f(1, 1, 0), .5f, new Vector3f(0, -1, 0), 25f);
    }

    public GlobalDirectionalLight(Vector3f color, float intensity, Vector3f direction, float size) {
        this.color = color;
        this.intensity = intensity;
        this.direction = direction;
        this.size = size;

        setDirection(direction);

        shadowShader = new ShadowShader();
    }

    FloatBuffer lBuffer = BufferUtils.createFloatBuffer(16 * 2 + 4 * 3);

    @Override
    public FloatBuffer toFloatBuffer() {
        shadowViewMatrix.toBuffer(lBuffer);
        shadowProjectionMatrix.toBuffer(lBuffer);
        color.toBuffer(lBuffer);
        lBuffer.put(intensity);
        direction.toBuffer(lBuffer);
        lBuffer.put(length);
        ambient.toBuffer(lBuffer);
        lBuffer.put(shadowTransparency);
        lBuffer.flip();
        return lBuffer;
    }

    @Override
    public int getLightType() {
        return Light.GLOBAL_DIRECTIONAL;
    }

    public Vector3f getDirection() {
        return direction;
    }

    // has 'memory leak'
    public void setDirection(Vector3f direction) {
        direction.normalize(this.direction);

        Matrix4f.shadowSkew(
                this.direction,
                origin,
                size, height, shadowViewMatrix);

        length = Matrix4f.shadowSkewLength(this.direction, size, height);
    }
}

