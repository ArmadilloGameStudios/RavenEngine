package com.raven.engine.scene.light;

import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.shader.ShadowShader;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;

import java.nio.FloatBuffer;

/**
 * Created by cookedbird on 11/30/17.
 */
public class DirectionalLight extends Light {
    public Vector3f color = new Vector3f();
    public float intensity = 1f;
    private Vector3f direction = new Vector3f();
    private Matrix4f shadowProjectionMatrix;
    private Matrix4f shadowViewMatrix;

    public DirectionalLight() {
        this(new Vector3f(1, 1, 0), .5f, new Vector3f(0, -1, 0), 25f);
    }

    public DirectionalLight(Vector3f color, float intensity, Vector3f direction, float size) {
        this.color = color;
        this.intensity = intensity;
        this.direction = direction;

        shadowProjectionMatrix = Matrix4f.orthographic(-size, size, -size, size, 1f, 80f);

        shadowViewMatrix = new Matrix4f().translate(0, 0, -30); // Matrix4f.direction(direction, null);

        shadowShader = new ShadowShader();
    }

    @Override
    public void toFloatBuffer(FloatBuffer buffer) {
        shadowViewMatrix.toBuffer(buffer);
        shadowProjectionMatrix.toBuffer(buffer);
        color.toBuffer(buffer);
        buffer.put(intensity);
        direction.toBuffer(buffer);
    }

    @Override
    public int getLightType() {
        return Light.DIRECTIONAL;
    }

    public Vector3f getDirection() {
        return direction;
    }

    // has 'memory leak'
    public void setDirection(Vector3f direction) {
        this.direction = direction.normalize();

        shadowViewMatrix = Matrix4f.lookAt(
                -this.direction.x, -this.direction.y, -this.direction.z,
                0f,0f,0f,
                0f,1f,0f);
        shadowViewMatrix = shadowViewMatrix.translate(this.direction.scale(-30f));
    }
}

