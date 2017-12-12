package com.raven.engine.scene.light;

import com.raven.engine.graphics3d.shader.ShadowShader;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

/**
 * Created by cookedbird on 11/30/17.
 */
public class DirectionalLight extends Light {
    public Vector3f color = new Vector3f();
    public float intensity = 1f;
    private Matrix4f shadowViewMatrix;
    private Matrix4f shadowProjectionMatrix;
    private Vector3f direction = new Vector3f();
    private Vector3f ambient = new Vector3f(.1f, .1f, .1f);

    public DirectionalLight() {
        this(new Vector3f(1, 1, 0), .5f, new Vector3f(0, -1, 0), 25f);
    }

    public DirectionalLight(Vector3f color, float intensity, Vector3f direction, float size) {
        this.color = color;
        this.intensity = intensity;
        this.direction = direction;

        shadowProjectionMatrix = Matrix4f.orthographic(-size, size, -size, size, 1f, 60f);

        shadowViewMatrix = new Matrix4f().translate(0, 0, -30); // Matrix4f.direction(direction, null);

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
        lBuffer.put(0.0f);
        ambient.toBuffer(lBuffer);
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
        this.direction = direction.normalize();

        shadowViewMatrix = Matrix4f.lookAt(
                -this.direction.x, -this.direction.y, -this.direction.z,
                0f, 0f, 0f,
                0f, 1f, 0f);
        shadowViewMatrix = shadowViewMatrix.translate(this.direction.scale(-30f));
    }
}

