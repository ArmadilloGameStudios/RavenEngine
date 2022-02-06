package com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget;

import com.armadillogamestudios.engine2d.GameProperties;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDrawBuffers;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

public class RenderTarget {

    private int framebuffer_handle;
    private int color_texture, id_texture, depth_texture;

    public RenderTarget(int color, int id, int depth, boolean full) {
        // fbo
        framebuffer_handle = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handle);

        // Color
        color_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + color);
        glBindTexture(GL_TEXTURE_2D, color_texture);

        if (full)
            GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                    GameProperties.getDisplayWidth(),
                    GameProperties.getDisplayHeight(),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
        else
            GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                    GameProperties.getWidth(),
                    GameProperties.getHeight(),
                    0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture, 0);

        // ID
        id_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + id);
        glBindTexture(GL_TEXTURE_2D, id_texture);

        if (full)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                    GameProperties.getDisplayWidth(),
                    GameProperties.getDisplayHeight(),
                    0, GL_RGB, GL_UNSIGNED_BYTE, 0);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                    GameProperties.getWidth(),
                    GameProperties.getHeight(),
                    0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, id_texture, 0);

        // Depth
        depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + depth);
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        if (full)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                    GameProperties.getDisplayWidth(),
                    GameProperties.getDisplayHeight(),
                    0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);
        else
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
                    GameProperties.getWidth(),
                    GameProperties.getHeight(),
                    0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depth_texture, 0);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("Render Target Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    public int getFramebufferHandle() {
        return framebuffer_handle;
    }

    public int getColorTexture() {
        return color_texture;
    }

    public int getIdTexture() {
        return id_texture;
    }

    public int getDepthTexture() {
        return depth_texture;
    }

    public void release() {
        glDeleteTextures(color_texture);
        glDeleteTextures(id_texture);
        glDeleteTextures(depth_texture);
        glDeleteFramebuffers(framebuffer_handle);
    }
}