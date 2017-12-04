package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.util.Matrix4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Created by cookedbird on 5/29/17.
 */
public class CombinationShader extends Shader {

    private int texture_world_color_location,
            texture_bloom_location,
            bloom_step_location;

    private Matrix4f projection_matrix = new Matrix4f();

    public CombinationShader() {
        super("vertex2.glsl", "combination_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        texture_world_color_location = glGetUniformLocation(getProgramHandel(), "worldColorTexture");

        texture_bloom_location = glGetUniformLocation(getProgramHandel(), "glowTexture");
        bloom_step_location = glGetUniformLocation(getProgramHandel(), "bloomStep");

        glLinkProgram(getProgramHandel());
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        // Bind the color and depth
        glUniform1i(texture_world_color_location, WorldShader.COLOR);

        // Bind the bloom
        glUniform1i(texture_bloom_location, BloomShader.GLOW);

        glUniform2f(bloom_step_location, 0f,
                1f / GameProperties.getScreenHeight());

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void endProgram() {

    }

    @Override
    protected Map<String, String> getGLSLVariableMap() {
        Map<String, String> map = new HashMap<>();

        map.put("NUM_SAMPLES", Integer.toString(GameProperties.getMultisampleCount()));

        return map;
    }

}
