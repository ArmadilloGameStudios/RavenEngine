package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameProperties;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class HighlightShader extends Shader {
    private int texture_highlight_location;

    public HighlightShader() {
        super("vertex2.glsl", "highlight_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        texture_highlight_location = glGetUniformLocation(getProgramHandel(), "highlight");
    }

    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glUniform1i(texture_highlight_location, WorldShader.HIGHLIGHT);

        glDisable(GL_DEPTH_TEST);

        // Blend
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void endProgram() {
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }
}
