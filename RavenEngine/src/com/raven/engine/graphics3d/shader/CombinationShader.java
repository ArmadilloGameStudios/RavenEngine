package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

/**
 * Created by cookedbird on 5/29/17.
 */
public class CombinationShader extends Shader {

    private int texture_world_color_location, texture_world_depth_location,
            texture_water_color_location, texture_water_depth_location,
            texture_bloom_location,
            bloom_step_location;

    public CombinationShader() {
        super("vertex2.glsl", "fragment2.glsl");

        texture_world_color_location = glGetUniformLocation(getProgramHandel(), "worldColorTexture");
        texture_world_depth_location = glGetUniformLocation(getProgramHandel(), "worldDepthTexture");
        texture_water_color_location = glGetUniformLocation(getProgramHandel(), "waterColorTexture");
        texture_water_depth_location = glGetUniformLocation(getProgramHandel(), "waterDepthTexture");

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
        glUniform1i(texture_world_depth_location, WorldShader.DEPTH);
        glUniform1i(texture_water_color_location, WaterShader.COLOR);
        glUniform1i(texture_water_depth_location, WaterShader.DEPTH);
        // Bind the bloom
        glUniform1i(texture_bloom_location, BloomShader.GLOW);

        glUniform2f(bloom_step_location, 0f,
                1f / GameEngine.getEngine().getGame().getHeight());

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glViewport(0, 0,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight());

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void endProgram() {

    }
}
