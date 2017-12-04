package com.raven.engine.scene.light;

import com.raven.engine.GameEngine;
import com.raven.engine.util.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 * Created by cookedbird on 11/30/17.
 */
public class DirectionalLight extends Light {
    private String name;
    public Vector3f color = new Vector3f();
    public float intensity = 1f;
    public Vector3f direction = new Vector3f();

    public DirectionalLight(String name) {
        this.name = name;
        this.color = new Vector3f(1,1,0);
        this.intensity = .5f;
        this.direction = new Vector3f(0, -1, 0);
    }

    public DirectionalLight(String name, Vector3f color, float intensity, Vector3f direction) {
        this.name = name;
        this.color = color;
        this.intensity = intensity;
        this.direction = direction;
    }

    @Override
    public void toBlock(int handel) {

    }
}
