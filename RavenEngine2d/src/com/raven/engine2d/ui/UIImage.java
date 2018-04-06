package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.scene.Scene;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class UIImage
        <S extends Scene,
         C extends UIContainer<S>>
        extends UIObject<S, C> {

    private int width, height, texture;

    private BufferedImage img;

    public UIImage(S scene, BufferedImage img) {
        super(scene);

        this.img = img;
        this.width = img.getWidth();
        this.height = img.getHeight();

        // To Byte Array
        int[] pixels = new int[width * height];
        img.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = pixels[y * width + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));    // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));     // Green component
                buffer.put((byte) (pixel & 0xFF));            // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component.
            }
        }

        buffer.flip();


        // Set Texture
        glActiveTexture(GL_TEXTURE0 + Shader.TEXTURE);
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
    public int getStyle() {
        return getParent().getStyle();
    }

    @Override
    public final float getWidth() {
        return width;
    }

    @Override
    public final float getHeight() {
        return height;
    }

    @Override
    public void draw(GameWindow window, Shader shader) {
//        shader.setProperties(this);

        glActiveTexture(GL_TEXTURE0 + Shader.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture);

        window.drawQuad();

        for (UIObject o : this.getChildren()) {
            o.draw(window, shader);
        }
    }

    @Override
    public void release() {
        super.release();

        glDeleteTextures(texture);
    }
}
