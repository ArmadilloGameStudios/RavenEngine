package com.raven.engine2d.graphics2d.sprite;

import com.raven.engine2d.graphics2d.shader.Shader;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.GL_RGBA32F;
import static org.lwjgl.opengl.GL30.GL_RGBA32I;

public class SpriteSheet {

    private int textureName;
    private int textureActiveLocation;
    private String filePath;
    public int height;
    public int width;

    public SpriteSheet(File f) {
        filePath = f.getPath();
    }

    public void load() {
        try {
            BufferedImage img = ImageIO.read(new File(filePath));

            this.width = img.getWidth();
            this.height = img.getHeight();

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

            textureActiveLocation = Shader.getNextTexture();

            glActiveTexture(GL_TEXTURE0 + textureActiveLocation);
            textureName = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureName);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                    width, height,
                    0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, 0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTextureActiveLocation() {
        return textureActiveLocation;
    }

    // TODO
    public void release() {
        glDeleteTextures(textureName);
    }

    public int getTextureName() {
        return textureName;
    }
}