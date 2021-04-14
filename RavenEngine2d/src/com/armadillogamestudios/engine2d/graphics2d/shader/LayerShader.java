package com.armadillogamestudios.engine2d.graphics2d.shader;

import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.engine2d.util.math.Vector4f;

import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.GL_MAX;
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

    private int sprite_sheet_location, rect_location, id_location, highlight_location, z_location, fade_location;

    private int[] buffers;

    public LayerShader(GameEngine engine, GameWindow window) {
        super("vertex.glsl", "fragment.glsl", engine);

        this.window = window;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        id_location = glGetUniformLocation(getProgramHandel(), "id");
        highlight_location = glGetUniformLocation(getProgramHandel(), "highlight");
        z_location = glGetUniformLocation(getProgramHandel(), "z");
        fade_location = glGetUniformLocation(getProgramHandel(), "fade");
        sprite_sheet_location = glGetUniformLocation(getProgramHandel(), "spriteSheet");
        rect_location = glGetUniformLocation(getProgramHandel(), "rect");

        buffers = new int[]{
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };

        useProgram();
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

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
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glClearDepth(0.0);
        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{0f, 0f, 0f, 0f});


        glActiveTexture(GL_TEXTURE0 + COLOR);
        glBindTexture(GL_TEXTURE_2D, renderTarget.getColorTexture());
        glActiveTexture(GL_TEXTURE0 + ID);
        glBindTexture(GL_TEXTURE_2D, renderTarget.getIdTexture());
        glActiveTexture(GL_TEXTURE0 + DEPTH);
        glBindTexture(GL_TEXTURE_2D, renderTarget.getDepthTexture());
    }

    public void clearDepthBuffer(RenderTarget renderTarget) {

        glBindFramebuffer(GL_FRAMEBUFFER, renderTarget.getFramebufferHandle());

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glClearDepth(0.0);
        glClear(GL_DEPTH_BUFFER_BIT);

//        glDrawBuffers(buffers);
    }

    @Override
    public void endProgram() {
//        glDisable(GL_BLEND);
    }

    private final static int isoHeight = 8, isoWidth = 16;

    private Vector4f spriteRect = new Vector4f();
    private int[] viewRect = new int[4];

    public void draw(ShaderTexture texture, SpriteAnimationState spriteAnimation, Vector2f position, Vector2f offset, Vector2f textureOffset, int id, float z, float fade, Highlight highlight, DrawStyle style) {

        float x;
        float y;

        switch (style) {
            default:
            case STANDARD:
                x = ((position.x * 16f + offset.x));
                y = ((position.y * 16f + offset.y));
                break;
            case STANDARD_12:
                x = ((position.x * 12f + offset.x));
                y = ((position.y * 12f + offset.y));
                break;
            case STANDARD_1:
                x = ((position.x + offset.x));
                y = ((position.y + offset.y));
                break;
            case ISOMETRIC:
                x = ((int) (position.y * isoWidth + position.x * isoWidth + offset.x));
                y = ((int) (position.y * isoHeight - position.x * isoHeight + offset.y));
                break;
            case UI:
                if (offset != null) {
                    x = (int) (position.x + offset.x);
                    y = (int) (position.y + offset.y);
                } else {
                    x = (int) position.x;
                    y = (int) position.y;
                }
                break;
        }

        if (spriteAnimation != null) {
            if (!spriteAnimation.getFlip()) {
                x += spriteAnimation.getXOffset();
                y += spriteAnimation.getYOffset();

                x *= GameProperties.getScaling();
                y *= GameProperties.getScaling();

                viewRect[0] = (int) Math.floor(x);
                viewRect[1] = (int) Math.floor(y);
                viewRect[2] = (int) (spriteAnimation.getWidth() * GameProperties.getScaling());
                viewRect[3] = (int) (spriteAnimation.getHeight() * GameProperties.getScaling());

                spriteRect.x = (float) spriteAnimation.getX() / (float) texture.getWidth();
                spriteRect.y = (float) spriteAnimation.getY() / (float) texture.getHeight();
                spriteRect.z = (float) spriteAnimation.getWidth() / (float) texture.getWidth();
                spriteRect.w = (float) spriteAnimation.getHeight() / (float) texture.getHeight();
            } else {
                x -= spriteAnimation.getXOffset();
                y += spriteAnimation.getYOffset();

                x *= GameProperties.getScaling();
                y *= GameProperties.getScaling();

                viewRect[0] = (int) Math.floor(x);
                viewRect[1] = (int) Math.floor(y);
                viewRect[2] = (int) (spriteAnimation.getWidth() * GameProperties.getScaling());
                viewRect[3] = (int) (spriteAnimation.getHeight() * GameProperties.getScaling());

                spriteRect.x = (float) (spriteAnimation.getX() + spriteAnimation.getWidth()) / (float) texture.getWidth();
                spriteRect.y = (float) spriteAnimation.getY() / (float) texture.getHeight();
                spriteRect.z = (float) -spriteAnimation.getWidth() / (float) texture.getWidth();
                spriteRect.w = (float) spriteAnimation.getHeight() / (float) texture.getHeight();
            }
        } else {

            x *= GameProperties.getScaling();
            y *= GameProperties.getScaling();

            viewRect[0] = (int) Math.floor(x);
            viewRect[1] = (int) Math.floor(y);
            viewRect[2] = (int) (texture.getWidth() * GameProperties.getScaling());
            viewRect[3] = (int) (texture.getHeight() * GameProperties.getScaling());

            if (textureOffset != null) {
                spriteRect.x = textureOffset.x;
                spriteRect.y = textureOffset.y;
            } else {
                spriteRect.x = 0;
                spriteRect.y = 0;
            }
            spriteRect.z = 1;
            spriteRect.w = 1;
        }

        // bail?
        if (viewRect[0] + viewRect[2] < 0 || viewRect[0] > GameProperties.getDisplayWidth() ||
            viewRect[1] + viewRect[3] < 0 || viewRect[1] > GameProperties.getDisplayHeight()) {
            return;
        }

        setWorldObjectID(id);

        glUniform1f(z_location, z);

        glUniform1f(fade_location, fade);

        if (highlight != null)
            glUniform4f(highlight_location, highlight.r, highlight.g, highlight.b, highlight.a);
        else
            glUniform4f(highlight_location, 0f, 0f, 0f, 0f);

        glActiveTexture(GL_TEXTURE0 + TEXTURE);
        glBindTexture(GL_TEXTURE_2D, texture.getTexture());
        glActiveTexture(GL_TEXTURE0);

        glUniform1i(sprite_sheet_location, TEXTURE);

        glViewport(
                viewRect[0],
                viewRect[1],
                viewRect[2],
                viewRect[3]);

        glUniform4f(rect_location,
                spriteRect.x,
                spriteRect.y,
                spriteRect.z,
                spriteRect.w);

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
