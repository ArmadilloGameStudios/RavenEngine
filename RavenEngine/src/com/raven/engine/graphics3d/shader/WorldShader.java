package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.input.Mouse;
import com.raven.engine.util.Vector3f;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.Highlight;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32.*;

/**
 * Created by cookedbird on 5/29/17.
 */
public class WorldShader extends Shader {

    public static final int
            COLOR = getNextTexture(),
            NORMAL = getNextTexture(),
            HIGHLIGHT = getNextTexture(),
            ID = getNextTexture(),
            DEPTH = getNextTexture();

    private int id_location, highlight_location;

    private int framebuffer_handel,
            color_texture,
            normal_texture,
            highlight_texture,
            id_texture,
            depth_texture;

    private IntBuffer buffers;

    public WorldShader() {
        super("world_vertex.glsl", "world_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_color");
        glBindAttribLocation(getProgramHandel(), 2, "vertex_normal");

        id_location = glGetUniformLocation(getProgramHandel(), "id");
        highlight_location = glGetUniformLocation(getProgramHandel(), "highlight");

        int blockIndex = glGetUniformBlockIndex(getProgramHandel(), "Matrices");
        glUniformBlockBinding(getProgramHandel(), blockIndex, MATRICES);

        int bfs[] = {
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // Normal
                GL_COLOR_ATTACHMENT2, // HIGHLIGHT
                GL_COLOR_ATTACHMENT3, // ID
        };

        buffers = BufferUtils.createIntBuffer(bfs.length);
        for (int i = 0; i < bfs.length; i++)
            buffers.put(bfs[i]);
        buffers.flip();

        // fbo
        framebuffer_handel = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        // FBO Textures
        // Color
        color_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D, color_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture, 0);

        // Normal
        normal_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + NORMAL);
        glBindTexture(GL_TEXTURE_2D, normal_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, normal_texture, 0);

        // Highlight
        highlight_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + HIGHLIGHT);
        glBindTexture(GL_TEXTURE_2D, highlight_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, highlight_texture, 0);

        // ID
        id_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + ID);
        glBindTexture(GL_TEXTURE_2D, id_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT3, id_texture, 0);

        // Depth
        depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth_texture, 0);

        // Draw buffers
        buffers.rewind();
        glDrawBuffers(buffers);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("World Shader Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    public void useProgram(Vector3f backgroundColor) {
        super.useProgram();

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the highlight buffer isn't colored
        glClearBufferfv(GL_COLOR, 2,
                new float[]{ 0f, 0f, 0f, 0f });

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 3,
                new float[]{ 0f, 0f, 0f, 0f });

        glEnable(GL_DEPTH_TEST);

        // Enable the custom mode attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void endProgram() {
        // Disable the custom mode attribute
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    private IntBuffer pixelReadBuffer = BufferUtils.createIntBuffer(1);
    public int getWorldObjectID() {
        Mouse mouse = GameEngine.getEngine().getMouse();

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadBuffer(GL_COLOR_ATTACHMENT3);
        glReadPixels(
                (int)mouse.getX(),
                GameProperties.getScreenHeight() - (int)mouse.getY(),
                1, 1,
                GL_RGB, GL_UNSIGNED_BYTE,
                pixelReadBuffer);

        int id = pixelReadBuffer.get();

        pixelReadBuffer.flip();

        return id;
    }

    public void setWorldObjectID(int id) {
        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            if (id != 0) {
                int r = (id & 0x000000FF) >> 0;
                int g = (id & 0x0000FF00) >> 8;
                int b = (id & 0x00FF0000) >> 16;

                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
            }
    }

    public void setHighlight(Highlight highlight) {
        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniform4f(highlight_location, highlight.r, highlight.g, highlight.b, highlight.a);
    }

    public int getDepthTexture() {
        return depth_texture;
    }

    public int getIDTexture() {
        return id_texture;
    }

    public int getFramebufferHandel() {
        return framebuffer_handel;
    }

}
