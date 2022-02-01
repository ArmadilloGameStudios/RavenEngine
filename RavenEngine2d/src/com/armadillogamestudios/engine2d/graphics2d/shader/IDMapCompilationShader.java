package com.armadillogamestudios.engine2d.graphics2d.shader;

import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.RenderTarget;
import com.armadillogamestudios.engine2d.util.math.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL40.glBlendEquationSeparatei;
import static org.lwjgl.opengl.GL40.glBlendFuncSeparatei;

public class IDMapCompilationShader extends Shader {

    private final RenderTarget compilationTarget;
    private final GameWindow window;

    private final int color_location, id_location, rect_location;

    public IDMapCompilationShader(GameEngine<?> engine, GameWindow window, CompilationShader compilationShader) {
        super("vertex.glsl", "compilation_id_overlay.glsl", engine);

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        rect_location = glGetUniformLocation(getProgramHandel(), "rect");
        color_location = glGetUniformLocation(getProgramHandel(), "colorTexture");
        id_location = glGetUniformLocation(getProgramHandel(), "id");

        this.window = window;
        this.compilationTarget = compilationShader.getCompilationTarget();
    }

    public RenderTarget getCompilationTarget() {
        return compilationTarget;
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_GREATER);

        glEnable(GL_BLEND);
        glBlendFuncSeparatei(0, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_SRC_ALPHA, GL_DST_ALPHA);
        glBlendEquationSeparatei(0, GL_FUNC_ADD, GL_FUNC_ADD);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }

    @Override
    public void endProgram() {

    }

    public void compile(RenderTarget from) {
        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());

        glActiveTexture(GL_TEXTURE0 + CompilationShader.TEXTURE_COLOR);
        glBindTexture(GL_TEXTURE_2D, from.getColorTexture());

        glActiveTexture(GL_TEXTURE0);

        glUniform1i(color_location, CompilationShader.TEXTURE_COLOR);

        int id = getEngine().getSecondID();
        int r = (id & 0x000000FF) >> 0;
        int g = (id & 0x0000FF00) >> 8;
        int b = (id & 0x00FF0000) >> 16;

        glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);

        glUniform4f(rect_location,
                0,
                0,
                1,
                -1);

        window.drawQuad();
    }
}
