package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.Highlight;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL31.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;
import static org.lwjgl.opengl.GL45.glNamedFramebufferDrawBuffers;

/**
 * Created by cookedbird on 5/29/17.
 */
public class WorldMSShader extends Shader {

    public static final int
            COLOR = getNextTexture(),
            ID = getNextTexture(),
            DEPTH = getNextTexture();

    private int id_location, highlight_location,
            textrue_shadow_location;

    private int ms_framebuffer_handel,
            ms_color_texture,
            ms_id_texture,
            ms_depth_texture;

    private IntBuffer buffers;

    public WorldMSShader() {
        super("world_vertex.glsl", "world_ms_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_normal");
        glBindAttribLocation(getProgramHandel(), 2, "vertex_texture");
        glBindAttribLocation(getProgramHandel(), 3, "vertex_color");

        id_location = glGetUniformLocation(getProgramHandel(), "id");
        highlight_location = glGetUniformLocation(getProgramHandel(), "highlight");
        textrue_shadow_location = glGetUniformLocation(getProgramHandel(), "shadowTexture");

        int blockIndex = glGetUniformBlockIndex(getProgramHandel(), "DirectionalLight");
        glUniformBlockBinding(getProgramHandel(), blockIndex, LIGHT);

        blockIndex = glGetUniformBlockIndex(getProgramHandel(), "Matrices");
        glUniformBlockBinding(getProgramHandel(), blockIndex, MATRICES);

        int bfs[] = {
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };

        buffers = BufferUtils.createIntBuffer(bfs.length);
        for (int i = 0; i < bfs.length; i++)
            buffers.put(bfs[i]);
        buffers.flip();

        int ms_count = GameEngine.getEngine().getWindow().getMultisampleCount();

        ms_framebuffer_handel = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, ms_framebuffer_handel);

        // MS FBO Textures
        // Color
        ms_color_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, ms_color_texture);

        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE,
                ms_count, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                true);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D_MULTISAMPLE, ms_color_texture, 0);
        glActiveTexture(GL_TEXTURE0);

        // ID
        ms_id_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + ID);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, ms_id_texture);

        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE,
                ms_count, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                true);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D_MULTISAMPLE, ms_id_texture, 0);
        glActiveTexture(GL_TEXTURE0);

        // Depth Texture
        ms_depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, ms_depth_texture);

        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE,
                ms_count, GL_DEPTH_COMPONENT32,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                true);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D_MULTISAMPLE, ms_depth_texture, 0);
        glActiveTexture(GL_TEXTURE0);

        // Draw buffers
        buffers.rewind();
        glDrawBuffers(buffers);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("FBO_MS Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    public void useProgram(Vector3f backgroundColor) {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glBindFramebuffer(GL_FRAMEBUFFER, ms_framebuffer_handel);

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{ 0f, 0f, 0f, 0f });

        glUniform1i(textrue_shadow_location, ShadowShader.DEPTH);

        // Enable the custom mode attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

//        buffers.rewind();
//        glDrawBuffers(buffers);
    }

    @Override
    public void endProgram() {
        // Disable the custom mode attribute
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
    }

    public void setWorldObjectID(int id) {
        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            if (id != 0) {
                int r = (id & 0x000000FF) >> 0;
                int g = (id & 0x0000FF00) >> 8;
                int b = (id & 0x00FF0000) >> 16;

                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
            }
    }

    public int getMSFramebuffer() {
        return ms_framebuffer_handel;
    }

    public void blitComplexValue() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, ms_framebuffer_handel);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        glDrawBuffer(GL_BACK);

        glBlitFramebuffer(
                0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                GL_COLOR_BUFFER_BIT, GL_NEAREST);
    }

    public void setHighlight(Highlight highlight) {
//        GameEngine.getEngine().getWindow().printErrors("Cat");
        glUniform4f(highlight_location, highlight.r, highlight.g, highlight.b, highlight.a);
//        GameEngine.getEngine().getWindow().printErrors("Dog");
//        System.out.println(highlight_location);
//        System.out.println(highlight.r);
//        System.out.println(highlight.g);
//        System.out.println(highlight.b);
//        System.out.println(highlight.a);

    }
}
