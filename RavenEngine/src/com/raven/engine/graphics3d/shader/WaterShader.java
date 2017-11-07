package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.util.Matrix4f;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL32.glTexImage2DMultisample;
import static org.lwjgl.opengl.GL45.glNamedFramebufferDrawBuffers;

/**
 * Created by cookedbird on 5/29/17.
 */
public class WaterShader extends Shader {

    public static final int
            COLOR = getNextTexture(),
            GLOW = getNextTexture(),
            DEPTH = getNextTexture();

    private int projection_location, model_location, view_location;
    private int framebuffer_handel, color_texture, bloom_texture, depth_texture;

    private Matrix4f projection_matrix = new Matrix4f(),
            model_matrix = new Matrix4f(),
            view_matrix = new Matrix4f();

    private IntBuffer buffers;

    public WaterShader() {
        super("water_vertex.glsl", "water_fragment.glsl");

        int bfs[] = {GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1 };
        buffers = BufferUtils.createIntBuffer(bfs.length);
        for (int i = 0; i < bfs.length; i++)
            buffers.put(bfs[i]);
        buffers.flip();

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_color");
        glBindAttribLocation(getProgramHandel(), 2, "vertex_normal");

        projection_location = glGetUniformLocation(getProgramHandel(), "P");
        model_location = glGetUniformLocation(getProgramHandel(), "M");
        view_location = glGetUniformLocation(getProgramHandel(), "V");

        glLinkProgram(getProgramHandel());

        // FBO
        framebuffer_handel = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        // FBO Textures
        // Color
        color_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D, color_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture, 0);
        glActiveTexture(GL_TEXTURE0);

        // Glow
        bloom_texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, bloom_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, bloom_texture, 0);

        // Draw buffers
        buffers.rewind();
        glDrawBuffers(buffers);

        // Depth
        depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight(),
                0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth_texture, 0);
        glActiveTexture(GL_TEXTURE0);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Water Shader Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handel);

        glViewport(0, 0,
                GameEngine.getEngine().getGame().getWidth(),
                GameEngine.getEngine().getGame().getHeight());

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        // Enable the custom mode attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glUniformMatrix4fv(projection_location, false, projection_matrix.toBuffer());
        glUniformMatrix4fv(model_location, false, model_matrix.toBuffer());
        glUniformMatrix4fv(view_location, false, view_matrix.toBuffer());
    }

    @Override
    public void endProgram() {
        // Disable the custom mode attribute
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    public void setProjectionMatrix(Matrix4f m) {
        projection_matrix = m;

        if(GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(projection_location, false, projection_matrix.toBuffer());
    }

    public void setViewMatrix(Matrix4f m) {
        model_matrix = m;

        if(GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(view_location, false, model_matrix.toBuffer());
    }

    public void setModelMatrix(Matrix4f m) {
        view_matrix = m;

        if(GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(view_location, false, view_matrix.toBuffer());
    }
    public int getColorTexture() {
        return color_texture;
    }

    public int getBloomTexture() {
        return bloom_texture;
    }

    public int getDepthTexture() {
        return depth_texture;
    }
}
