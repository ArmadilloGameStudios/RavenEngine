package com.armadillogamestudios.engine2d.graphics2d.shader;

import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.IDMapRenderTarget;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.RenderTarget;
import com.armadillogamestudios.engine2d.input.Mouse;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL40.glBlendEquationSeparatei;
import static org.lwjgl.opengl.GL40.glBlendFuncSeparatei;

public class CompilationShader extends Shader {
    public static final int
            COLOR = getNextTextureID("Color"),
            ID = getNextTextureID("ID"),
            DEPTH = getNextTextureID("Depth"),
            TEXTURE_COLOR = Shader.getNextTextureID("Texture"),
            TEXTURE_ID = Shader.getNextTextureID("Texture"),
            TEXTURE_DEPTH = Shader.getNextTextureID("Texture");

    private final int color_location, id_location, depth_location, rect_location;
    private final int[] buffers;

    private final RenderTarget compilationTarget;
    private final GameWindow window;

    public CompilationShader(GameEngine<?> engine, GameWindow window) {
        super("vertex.glsl", "compilation.glsl", engine);

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        rect_location = glGetUniformLocation(getProgramHandel(), "rect");
        color_location = glGetUniformLocation(getProgramHandel(), "colorTexture");
        id_location = glGetUniformLocation(getProgramHandel(), "idTexture");
        depth_location = glGetUniformLocation(getProgramHandel(), "depthTexture");

        buffers = new int[] {
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };

        compilationTarget = new RenderTarget(COLOR, ID, DEPTH, true);
        this.window = window;
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

        glDrawBuffers(buffers);
    }

    @Override
    public void endProgram() {
        // glDisable(GL_BLEND);
    }

    public void clear(Vector3f backgroundColor) {

        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());

        glDrawBuffers(buffers);

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glClearDepth(0.0);
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{0f, 0f, 0f, 0f});

        glDrawBuffers(buffers);
    }

    public void clearDepthBuffer() {
//        glDisable(GL_BLEND);

        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());
        glDrawBuffers(buffers);

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glClearDepth(0.0);
        glClear(GL_DEPTH_BUFFER_BIT);
    }

    private IntBuffer pixelReadBuffer = BufferUtils.createIntBuffer(1);
    public int getWorldObjectID() {
        Mouse mouse = getEngine().getMouse();

        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadBuffer(GL_COLOR_ATTACHMENT1);
//        glReadPixels(
//                (int) ((mouse.getX() * GameProperties.getDisplayWidth()) / (GameProperties.getDisplayWidth() * GameProperties.getScaling())),
//                (GameProperties.getDisplayHeight() - (int) mouse.getY()) * GameProperties.getDisplayHeight() / (GameProperties.getDisplayHeight() * GameProperties.getScaling()),
//                1, 1,
//                GL_RGB, GL_UNSIGNED_BYTE,
//                pixelReadBuffer);
        glReadPixels(
                (int) ((mouse.getX() * GameProperties.getDisplayWidth()) / (GameProperties.getDisplayWidth())),
                (GameProperties.getDisplayHeight() - (int) mouse.getY()) * GameProperties.getDisplayHeight() / (GameProperties.getDisplayHeight()),
                1, 1,
                GL_RGB, GL_UNSIGNED_BYTE,
                pixelReadBuffer);


        int id = pixelReadBuffer.get();

        pixelReadBuffer.flip();

        return id;
    }

    public void blitToScreen() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, compilationTarget.getFramebufferHandle());
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        glDrawBuffer(GL_BACK);

        glBlitFramebuffer(
                0, 0,
                GameProperties.getDisplayWidth(),// / GameProperties.getScaling(),
                GameProperties.getDisplayHeight(),// / GameProperties.getScaling(),
                0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight(),
                GL_COLOR_BUFFER_BIT, GL_NEAREST);
    }

    public void compile(RenderTarget from) {
        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());

        glActiveTexture(GL_TEXTURE0 + TEXTURE_COLOR);
        glBindTexture(GL_TEXTURE_2D, from.getColorTexture());

        glActiveTexture(GL_TEXTURE0 + TEXTURE_ID);
        glBindTexture(GL_TEXTURE_2D, from.getIdTexture());

        glActiveTexture(GL_TEXTURE0 + TEXTURE_DEPTH);
        glBindTexture(GL_TEXTURE_2D, from.getDepthTexture());

        glActiveTexture(GL_TEXTURE0);

        glUniform1i(color_location, TEXTURE_COLOR);
        glUniform1i(id_location, TEXTURE_ID);
        glUniform1i(depth_location, TEXTURE_DEPTH);

        glUniform4f(rect_location,
                0,
                0,
                1,
                -1);

        window.drawQuad();
    }

    public void compileColorOnly(RenderTarget from) {
        glBindFramebuffer(GL_FRAMEBUFFER, compilationTarget.getFramebufferHandle());

        glActiveTexture(GL_TEXTURE0 + TEXTURE_COLOR);
        glBindTexture(GL_TEXTURE_2D, from.getColorTexture());

        glActiveTexture(GL_TEXTURE0);

        glUniform1i(color_location, TEXTURE_COLOR);

        glUniform4f(rect_location,
                0,
                0,
                1,
                -1);

        window.drawQuad();
    }

    public void drawColorOnly() {
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
    }
}
