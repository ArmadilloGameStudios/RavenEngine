package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.scene.Scene;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class UITexture
        extends ShaderTexture {

    private int textureActiveLocation;
    private int width, height, texture;

    public UITexture(GameEngine engine, int width, int height) {
        super(engine);

        this.width = width;
        this.height = height;
    }

    @Override
    public void load(Scene scene) {
        // Set Texture
        if (textureActiveLocation == 0)
            textureActiveLocation = Shader.getNextTextureID();

        glActiveTexture(GL_TEXTURE0 + textureActiveLocation);

        if (texture == 0)
            texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);

        scene.addLoadedShaderTexture(this);
    }

    @Override
    public int getTextureActiveLocation() {
        return textureActiveLocation;
    }

    @Override
    public int getTexture() {
        return texture;
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public void release() {
        // TODO make sure this is correct
        textureActiveLocation = 0;
        texture = 0;
        glDeleteTextures(texture);
    }
}
