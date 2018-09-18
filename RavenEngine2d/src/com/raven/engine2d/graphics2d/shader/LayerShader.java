package com.raven.engine2d.graphics2d.shader;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.input.Mouse;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.worldobject.Highlight;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL40.glBlendEquationSeparatei;
import static org.lwjgl.opengl.GL40.glBlendFuncSeparatei;

public class LayerShader extends Shader {

    public static final int
            COLOR = getNextTextureID("Color"),
            ID = getNextTextureID("ID"),
            DEPTH = getNextTextureID("Depth"),
            TEXTURE = Shader.getNextTextureID("Texture");

    private GameWindow window;

    private int sprite_sheet_location, position_location, rect_location, id_location, highlight_location, z_location;

    private int[] buffers;

    public LayerShader(GameEngine engine, GameWindow window) {
        super("vertex.glsl", "fragment.glsl", engine);

        this.window = window;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        id_location = glGetUniformLocation(getProgramHandel(), "id");
        highlight_location = glGetUniformLocation(getProgramHandel(), "highlight");
        z_location = glGetUniformLocation(getProgramHandel(), "z");
        sprite_sheet_location = glGetUniformLocation(getProgramHandel(), "spriteSheet");
        rect_location = glGetUniformLocation(getProgramHandel(), "rect");
        position_location = glGetUniformLocation(getProgramHandel(), "position");

        buffers = new int[] {
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glViewport(0, 0,
                GameProperties.getScreenWidth() / 2,
                GameProperties.getScreenHeight() / 2);

        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_GREATER);

        glEnable(GL_BLEND);
        glBlendFuncSeparatei(0, GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        glBlendEquationSeparatei(0, GL_FUNC_ADD, GL_MAX);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawBuffers(buffers);
    }

    public void clear(RenderTarget renderTarget, Vector3f backgroundColor) {

        glBindFramebuffer(GL_FRAMEBUFFER, renderTarget.getFramebufferHandle());

        glDrawBuffers(buffers);

        glViewport(0, 0,
                GameProperties.getScreenWidth() / GameProperties.getScaling(),
                GameProperties.getScreenHeight() / GameProperties.getScaling());

        glClearDepth(0.0);
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{0f, 0f, 0f, 0f});
    }

    public void clearDepthBuffer(RenderTarget renderTarget) {

        glBindFramebuffer(GL_FRAMEBUFFER, renderTarget.getFramebufferHandle());

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearDepth(0.0);
        glClear(GL_DEPTH_BUFFER_BIT);

//        glDrawBuffers(buffers);
    }

    @Override
    public void endProgram() {
//        glDisable(GL_BLEND);
    }

    private float isoHeight = 16, isoWidth = 32;

    public void draw(ShaderTexture texture, RenderTarget target, SpriteAnimationState spriteAnimation, Vector2f position, Vector2f offset, int id, float z, Highlight highlight, DrawStyle style) {
        setWorldObjectID(id);

        glUniform1f(z_location, z);

        if (highlight != null)
            glUniform4f(highlight_location, highlight.r, highlight.g, highlight.b, highlight.a);
        else
            glUniform4f(highlight_location, 0f, 0f, 0f, 0f);

        float x;
        float y;

        switch (style) {
            default:
            case ISOMETRIC:
                x =  position.y * isoWidth + position.x * isoWidth + offset.x;
                y = position.y * isoHeight - position.x * isoHeight + offset.y;
                break;
            case UI:
                if (offset != null) {
                    x = position.x + offset.x * 2f;
                    y = position.y + offset.y * 2f;
                } else {
                    x = position.x;
                    y = position.y;
                }
                break;
        }

        glUniform2f(position_location,
                x / GameProperties.getScreenWidth() / GameProperties.getScaling() + .5f,
                y / GameProperties.getScreenHeight() / GameProperties.getScaling() + .5f);


        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D, target.getColorTexture());
        glActiveTexture(GL_TEXTURE0 + ID);
        glBindTexture(GL_TEXTURE_2D, target.getIdTexture());
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, target.getDepthTexture());
        glActiveTexture(GL_TEXTURE0 + TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture.getTexture());
        glActiveTexture(GL_TEXTURE0);

        glUniform1i(sprite_sheet_location, TEXTURE);

        if (spriteAnimation != null) {
            if (!spriteAnimation.getFlip()) {
                x += spriteAnimation.getXOffset();
                y += spriteAnimation.getYOffset();

                glViewport((int) Math.floor(x / 2), (int) Math.floor(y / 2), spriteAnimation.getWidth(), spriteAnimation.getHeight());

                glUniform4f(rect_location,
                        (float) spriteAnimation.getX() / (float) texture.getWidth(),
                        (float) spriteAnimation.getY() / (float) texture.getHeight(),
                        (float) spriteAnimation.getWidth() / (float) texture.getWidth(),
                        (float) spriteAnimation.getHeight() / (float) texture.getHeight());
            } else {
                x -= spriteAnimation.getXOffset();
                y += spriteAnimation.getYOffset();

                glViewport((int) Math.floor(x / 2), (int) Math.floor(y / 2), spriteAnimation.getWidth(), spriteAnimation.getHeight());

                glUniform4f(rect_location,
                        (float) (spriteAnimation.getX() + spriteAnimation.getWidth()) / (float) texture.getWidth(),
                        (float) spriteAnimation.getY() / (float) texture.getHeight(),
                        (float) -spriteAnimation.getWidth() / (float) texture.getWidth(),
                        (float) spriteAnimation.getHeight() / (float) texture.getHeight());
            }
        } else {
            glViewport((int) Math.floor(x / 2), (int) Math.floor(y / 2), texture.getWidth(), texture.getHeight());

            glUniform4f(rect_location,
                    0,
                    0,
                    1,
                    1);
        }

        window.drawQuad();
    }

    public void setWorldObjectID(int id) {
//        if (getEngine().getWindow().getActiveShader() == this) {
        if (id != 0) {
            int r = (id & 0x000000FF) >> 0;
            int g = (id & 0x0000FF00) >> 8;
            int b = (id & 0x00FF0000) >> 16;

            glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);

            glEnable(GL_DEPTH_TEST);
            glDrawBuffers(buffers);
        } else {
//            glDisable(GL_DEPTH_TEST);
            glDrawBuffer(GL_COLOR_ATTACHMENT0);
        }
    }
}
