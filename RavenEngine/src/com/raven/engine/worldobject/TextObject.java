package com.raven.engine.worldobject;

import com.raven.engine.graphics3d.shader.HUDShader;
import com.raven.engine.util.math.Vector4f;
import com.sun.prism.impl.BufferUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class TextObject {
	private String text;

	private int width, height, texture;

	private BufferedImage img;

	private Font font = new Font( "SansSerif", Font.PLAIN, 12 );
	private Vector4f
            fontColor = new Vector4f(1f, 1f, 1f, 1f),
            backgroundColor = new Vector4f(.25f, .25f, .25f, 1f);

	public TextObject(int width, int height) {
	    this.width = width;
	    this.height = height;

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Default Texture
        glActiveTexture(GL_TEXTURE0 + HUDShader.TEXTURE);
        texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8,
                width, height,
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
	}

	public void updateTexture() {
        // Draw Text
        Graphics2D g = img.createGraphics();

        g.setColor(
                new Color(
                        (int)(255 * backgroundColor.z),
                        (int)(255 * backgroundColor.y),
                        (int)(255 * backgroundColor.x)));
        g.fillRect(0,0,width, height);

        g.setColor(
                new Color(
                        (int)(255 * fontColor.z),
                        (int)(255 * fontColor.y),
                        (int)(255 * fontColor.x)));
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(font);

        String[] lines = this.text.split("\n");

        FontMetrics fm = g.getFontMetrics();

        float textHeight = fm.getHeight();
        float centerHeight = this.height / 2f;
        float centerWidth = this.width / 2f;


        for (int i = 0; i < lines.length; i++) {
            float len = fm.stringWidth(lines[i]);

            g.drawString(lines[i],
                    centerWidth - len / 2f,
                    centerHeight + textHeight * .25f + textHeight * i - (lines.length - 1) * textHeight * .5f);
        }

        // To Byte Array
        DataBufferInt dbb = (DataBufferInt)img.getRaster().getDataBuffer();
        int[] data = dbb.getData();
        IntBuffer pixels = BufferUtil.newIntBuffer(data.length);
        pixels.put(data);
        pixels.flip();

        // Set Texture
        glActiveTexture(GL_TEXTURE0 + HUDShader.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                width, height,
                0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glActiveTexture(GL_TEXTURE0);
    }

	public void setFont(Font font) {
	    this.font = font;
    }

	public void setText(String text) {
		this.text = text;
	}

	public void draw() {
	    glActiveTexture(GL_TEXTURE0 + HUDShader.TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void release() {
        glDeleteTextures(texture);
    }

    public void setBackgroundColor(Vector4f backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
