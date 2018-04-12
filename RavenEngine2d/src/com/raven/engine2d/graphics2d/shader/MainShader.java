package com.raven.engine2d.graphics2d.shader;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimation;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.input.Mouse;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import sun.java2d.pipe.SpanClipRenderer;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT2;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT3;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;
import static org.lwjgl.opengl.GL40.glBlendFuncSeparatei;

public class MainShader extends Shader {

    public static final int
            COLOR = getNextTexture(),
            ID = getNextTexture(),
            DEPTH = getNextTexture();

    private GameWindow window;

    private int framebuffer_handle;
    private int color_texture, id_texture, depth_texture;

    private int sprite_sheet_location, rect_location, id_location;

    private IntBuffer buffers;

    public MainShader(GameWindow window) {
        super("vertex.glsl", "terrain_fragment.glsl");

        this.window = window;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        id_location = glGetUniformLocation(getProgramHandel(), "id");
        sprite_sheet_location = glGetUniformLocation(getProgramHandel(), "spriteSheet");
        rect_location = glGetUniformLocation(getProgramHandel(), "rect");

        int bfs[] = {
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };

        buffers = BufferUtils.createIntBuffer(bfs.length);
        for (int i = 0; i < bfs.length; i++)
            buffers.put(bfs[i]);
        buffers.flip();

        // fbo
        framebuffer_handle = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handle);


        // Color
        color_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D, color_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, color_texture, 0);

        // ID
        id_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + ID);
        glBindTexture(GL_TEXTURE_2D, id_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1, id_texture, 0);

        // Depth
        depth_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, depth_texture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT,
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
            System.out.println("Main Shader Failed: 0x"
                    + Integer.toHexString(glCheckFramebufferStatus(GL_FRAMEBUFFER)));
        }
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handle);

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glEnable(GL_BLEND);
        glBlendFuncSeparatei(0, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawBuffers(buffers);
    }

    public void clear(Vector3f backgroundColor) {

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{0f, 0f, 0f, 0f});

        glDrawBuffers(buffers);
    }

    @Override
    public void endProgram() {
        glDisable(GL_BLEND);
    }

    public void draw(SpriteSheet sheet, SpriteAnimationState spriteAnimation, Vector2f position, Vector2f offset, int id, DrawStyle style) {
        setWorldObjectID(id);

        switch (style) {
            case ISOMETRIC:
                drawIsometric(sheet, spriteAnimation, position, offset);
                break;
            case UI:
                drawUI(sheet, spriteAnimation, position);
                break;
        }
    }

    private float isoHeight = 16, isoWidth = 31;

    private void drawIsometric(SpriteSheet sheet, SpriteAnimationState spriteAnimation, Vector2f position, Vector2f offset) {
        glUniform1i(sprite_sheet_location, sheet.getTextureActiveLocation());

        float x = position.y * isoWidth - position.x * isoWidth + offset.x;
        float y = position.y * isoHeight + position.x * isoHeight + offset.y;

        glViewport((int) x, (int) y, spriteAnimation.getWidth() * 2, spriteAnimation.getHeight() * 2);

        glUniform4f(rect_location,
                (float) spriteAnimation.getX() / (float) sheet.width,
                (float) spriteAnimation.getY() / (float) sheet.height,
                (float) spriteAnimation.getWidth() / (float) sheet.width,
                (float) spriteAnimation.getHeight() / (float) sheet.height);

        window.drawQuad();
    }

    private void drawUI(SpriteSheet sheet, SpriteAnimationState spriteAnimation, Vector2f position) {
        // TODO

        glUniform1i(sprite_sheet_location, sheet.getTextureActiveLocation());

        float x = position.x;
        float y = position.y;

        glViewport((int) x, (int) y, spriteAnimation.getWidth() * 2, spriteAnimation.getHeight() * 2);

        glUniform4f(rect_location,
                (float) spriteAnimation.getX() / (float) sheet.width,
                (float) spriteAnimation.getY() / (float) sheet.height,
                (float) spriteAnimation.getWidth() / (float) sheet.width,
                (float) spriteAnimation.getHeight() / (float) sheet.height);

        window.drawQuad();
    }


    private IntBuffer pixelReadBuffer = BufferUtils.createIntBuffer(1);

    public int getWorldObjectID() {
        Mouse mouse = GameEngine.getEngine().getMouse();

        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer_handle);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadBuffer(GL_COLOR_ATTACHMENT1);
        glReadPixels(
                (int) mouse.getX(),
                GameProperties.getScreenHeight() - (int) mouse.getY(),
                1, 1,
                GL_RGB, GL_UNSIGNED_BYTE,
                pixelReadBuffer);

        int id = pixelReadBuffer.get();

        pixelReadBuffer.flip();

        return id;
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

    public void blitToScreen() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer_handle);
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
}
