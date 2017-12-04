package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.util.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
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
public class WorldShader extends Shader {

    public static final int
            COLOR = getNextTexture(),
            GLOW = getNextTexture(),
            ID = getNextTexture(),
            DEPTH = getNextTexture();

    private int id_location;
    private int ms_framebuffer_handel, ms_renderbuffer_handel, ms_color_texture, ms_glow_texture,
            ms_id_texture, ms_depth_texture;
    private int framebuffer_handel, renderbuffer_handel, color_texture, bloom_texture,
            id_texture, depth_texture;

    private Matrix4f projection_matrix = new Matrix4f(),
            model_matrix = new Matrix4f(),
            view_matrix = new Matrix4f();

    private IntBuffer buffers;

    public WorldShader() {
        super("world_vertex.glsl", "world_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_color");
        glBindAttribLocation(getProgramHandel(), 2, "vertex_normal");

        id_location = glGetUniformLocation(getProgramHandel(), "id");

        int blockIndex = glGetUniformBlockIndex(getProgramHandel(), "DirectionalLight");
        glUniformBlockBinding(getProgramHandel(), blockIndex, DIRECTIONAL_LIGHT);

        blockIndex = glGetUniformBlockIndex(getProgramHandel(), "Matrices");
        glUniformBlockBinding(getProgramHandel(), blockIndex, MATRICES);

        int bfs[] = {GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2};
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

        // Glow
        ms_glow_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + GLOW);
        glBindTexture(GL_TEXTURE_2D_MULTISAMPLE, ms_glow_texture);

        glTexImage2DMultisample(GL_TEXTURE_2D_MULTISAMPLE,
                ms_count, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                true);

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, GL_TEXTURE_2D_MULTISAMPLE, ms_glow_texture, 0);
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

        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, GL_TEXTURE_2D_MULTISAMPLE, ms_id_texture, 0);

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

        // Depth
//        ms_renderbuffer_handel = glGenRenderbuffers();
//        glBindRenderbuffer(GL_RENDERBUFFER, ms_renderbuffer_handel);
//        glRenderbufferStorageMultisample(GL_RENDERBUFFER, ms_count, GL_DEPTH_COMPONENT,
//                GameEngine.getEngine().getGame().getWidth(),
//                GameEngine.getEngine().getGame().getHeight());
//        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
//                GL_RENDERBUFFER, ms_renderbuffer_handel);


        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("FBO_MS Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }

        // resolve ms
        framebuffer_handel = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        // FBO Textures
        // Color
        color_texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, color_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture, 0);

        // Glow
        bloom_texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, bloom_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, bloom_texture, 0);

        // ID
        id_texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2, id_texture, 0);

        // Depth
        depth_texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32,
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

    @Override
    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glBindFramebuffer(GL_FRAMEBUFFER, ms_framebuffer_handel);

//        glViewport(0, 0,
//                GameProperties.getScreenWidth(),
//                GameProperties.getScreenHeight());

        glClearColor(0.6f, 0.7f, 1f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 2,
                new float[]{ 0f, 0f, 0f, 0f });


        // Enable the custom mode attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glEnable(GL_CLIP_DISTANCE0);
    }

    @Override
    public void endProgram() {
        // Disable the custom mode attribute
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glDisable(GL_CLIP_DISTANCE0);
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

    public int getColorTexture() {
        return ms_color_texture;
    }

    public int getBloomTexture() {
        return bloom_texture;
    }

    public int getDepthTexture() {
        return ms_depth_texture;
    }

    public int getFrameBuffer() {
        return ms_framebuffer_handel;
    }

    public void blitFramebuffer() {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        glDrawBuffer(GL_BACK);

        glBindFramebuffer(GL_READ_FRAMEBUFFER, ms_framebuffer_handel);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        glBlitFramebuffer(
                0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT, GL_NEAREST);
    }
}
