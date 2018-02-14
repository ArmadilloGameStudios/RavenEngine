package com.raven.engine.worldobject;

import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.shader.HUDShader;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import com.sun.prism.impl.BufferUtil;
import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public abstract class HUDImage
        <S extends Scene,
         C extends HUDContainer<S>>
        extends HUDObject<S, C> {

    private int width, height, texture;

    private BufferedImage img;

    public HUDImage(S scene, BufferedImage img) {
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
        glActiveTexture(GL_TEXTURE0 + HUDShader.TEXTURE);
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        System.out.println("Dog");

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8,
                width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

        System.out.println("Mouse");

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);

        System.out.println("Cat");
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
    public void draw(GameWindow window, HUDShader shader) {
        shader.setProperties(this);

        glActiveTexture(GL_TEXTURE0 + HUDShader.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture);

        window.drawQuad();

        for (HUDObject o : this.getChildren()) {
            o.draw(window, shader);
        }
    }

    @Override
    public void release() {
        super.release();

        glDeleteTextures(texture);
    }
}