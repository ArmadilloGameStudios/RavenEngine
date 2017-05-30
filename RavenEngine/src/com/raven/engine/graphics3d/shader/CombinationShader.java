package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Created by cookedbird on 5/29/17.
 */
public class CombinationShader extends Shader {

    int texture_color_location, texture_bloom_location, bloom_step_location, screen_size_location;
    int color_texture, bloom_texture;

    public CombinationShader(int color_texture, int bloom_texture) {
        super("vertex2.glsl", "fragment2.glsl");

        this.color_texture = color_texture;
        this.bloom_texture = bloom_texture;

        texture_color_location = glGetUniformLocation(getProgramHandel(), "colorTexture");
        texture_bloom_location = glGetUniformLocation(getProgramHandel(), "glowTexture");
        bloom_step_location = glGetUniformLocation(getProgramHandel(), "bloomStep");

        screen_size_location = glGetUniformLocation(getProgramHandel(), "screen_size");

        glLinkProgram(getProgramHandel());

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, color_texture);

        glActiveTexture(GL_TEXTURE0 + 1);
        glBindTexture(GL_TEXTURE_2D, bloom_texture);
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        // Bind the color
        glUniform1i(texture_color_location, 0);

        // Bind the bloom
        glUniform1i(texture_bloom_location, 1);

        glUniform2f(bloom_step_location, 0f,
                1f / GameEngine.getEngine().getGame().getHeight());

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glViewport(0, 0,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight());

        glClearColor(0.0f, 0.0f, 1.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void endProgram() {

    }
}
