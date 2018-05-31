package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class UITexture
        extends ShaderTexture {

    private int textureActiveLocation;
    private int width, height, texture;

    private BufferedImage img;
    private Graphics2D imgGraphics;

    public UITexture(int width, int height) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.width = width;
        this.height = height;
    }

    public UITexture(int width, int height, String src) {
        this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        this.width = width;
        this.height = height;

        drawImage(src);
    }

    public void drawImage(String src) {
        System.out.println(src);
        SpriteSheet background = GameEngine.getEngine().getSpriteSheet(src);

        if (imgGraphics == null)
            imgGraphics = img.createGraphics();

        imgGraphics.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        imgGraphics.drawImage(background.getImage(), 0, 0, null);
    }

    @Override
    public void load() {

        // To Byte Array
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));    // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));     // Green component
                buffer.put((byte) (pixel & 0xFF));            // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component.
            }
        }

        buffer.flip();

        // Set Texture
        if (textureActiveLocation == 0)
            textureActiveLocation = Shader.getNextTextureID();

        glActiveTexture(GL_TEXTURE0 + textureActiveLocation);

        if (texture == 0)
            texture = glGenTextures();

        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
    }

    @Override
    public int getTextureActiveLocation() {
        return textureActiveLocation;
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
        glDeleteTextures(texture);
    }

    public BufferedImage getImage() {
        return img;
    }
}
