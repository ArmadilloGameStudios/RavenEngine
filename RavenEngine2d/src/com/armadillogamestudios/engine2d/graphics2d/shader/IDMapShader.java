package com.armadillogamestudios.engine2d.graphics2d.shader;

import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.IDMapRenderTarget;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.RenderTarget;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.input.Mouse;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.engine2d.util.math.Vector4f;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import org.lwjgl.BufferUtils;

import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.GL_MAX;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL40.glBlendEquationSeparatei;
import static org.lwjgl.opengl.GL40.glBlendFuncSeparatei;

public class IDMapShader extends Shader {
    public static final int
            ID = getNextTextureID("ID"),
            COLOR = getNextTextureID("Color"),
            DEPTH = getNextTextureID("Depth"),
            TEXTURE = Shader.getNextTextureID("Texture");

    private final static int isoHeight = 8, isoWidth = 16;
    private final GameWindow window;
    private final int sprite_sheet_location;
    private final int rect_location;
    private final int highlight_id_location;
    private final Vector4f spriteRect = new Vector4f();
    private final int[] viewRect = new int[4];
    private final RenderTarget renderTarget;
    private final int[] buffers;

    public IDMapShader(GameEngine<?> engine, GameWindow window) {
        super("vertex.glsl", "idmap.glsl", engine);

        this.window = window;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_textures_coords");

        sprite_sheet_location = glGetUniformLocation(getProgramHandel(), "spriteSheet");
        rect_location = glGetUniformLocation(getProgramHandel(), "rect");
        highlight_id_location = glGetUniformLocation(getProgramHandel(), "highlight_id");

        buffers = new int[]{
                GL_COLOR_ATTACHMENT0, // Color
                GL_COLOR_ATTACHMENT1, // ID
        };

        renderTarget = new RenderTarget(IDMapShader.COLOR, IDMapShader.ID, IDMapShader.DEPTH, true);

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

        glDisable(GL_BLEND);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawBuffers(buffers);
    }

    public void clear() {

        glBindFramebuffer(GL_FRAMEBUFFER, renderTarget.getFramebufferHandle());

        glDrawBuffers(buffers);

        glViewport(0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight());

        glClearDepth(0.0);
        glClearColor(0f, 0f, 0f, 0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // make sure the id buffer isn't colored
        glClearBufferfv(GL_COLOR, 1,
                new float[]{0f, 0f, 0f, 0f});

        glDrawBuffers(buffers);

//        glActiveTexture(GL_TEXTURE0 + COLOR);
//        glBindTexture(GL_TEXTURE_2D, renderTarget.getColorTexture());
//        glActiveTexture(GL_TEXTURE0 + ID);
//        glBindTexture(GL_TEXTURE_2D, renderTarget.getIdTexture());
    }

    @Override
    public void endProgram() {

    }

    public void draw(ShaderTexture texture, Vector2f position, Vector2f offset, Vector2f textureOffset, DrawStyle style) {
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


        // bail?
        if (viewRect[0] + viewRect[2] < 0 || viewRect[0] > GameProperties.getDisplayWidth() ||
                viewRect[1] + viewRect[3] < 0 || viewRect[1] > GameProperties.getDisplayHeight()) {
            return;
        }

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

    private IntBuffer pixelReadBuffer = BufferUtils.createIntBuffer(1);
    public int getIDMapID() {
        Mouse mouse = getEngine().getMouse();

        glBindFramebuffer(GL_FRAMEBUFFER, renderTarget.getFramebufferHandle());
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadBuffer(GL_COLOR_ATTACHMENT1);
//        glReadPixels(
//                (int) ((mouse.getX() * GameProperties.getDisplayWidth()) / (GameProperties.getDisplayWidth() * GameProperties.getScaling())),
//                (GameProperties.getDisplayHeight() - (int) mouse.getY()) * GameProperties.getDisplayHeight() / (GameProperties.getDisplayHeight() * GameProperties.getScaling()),
//                1, 1,
//                GL_RGB, GL_UNSIGNED_BYTE,
//                pixelReadBuffer);
        glReadPixels(
                (int) ((mouse.getX() * GameProperties.getDisplayWidth()) / (GameProperties.getDisplayWidth())),
                (GameProperties.getDisplayHeight() - (int) mouse.getY()) * GameProperties.getDisplayHeight() / (GameProperties.getDisplayHeight()),
                1, 1,
                GL_RGB, GL_UNSIGNED_BYTE,
                pixelReadBuffer);


        int id = pixelReadBuffer.get();

        pixelReadBuffer.flip();

        return id;
    }

    public RenderTarget getRenderTarget() {
        return renderTarget;
    }

    public void blitToScreen() {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, renderTarget.getFramebufferHandle());
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
        glDrawBuffer(GL_BACK);

        glBlitFramebuffer(
                0, 0,
                GameProperties.getDisplayWidth(),// / GameProperties.getScaling(),
                GameProperties.getDisplayHeight(),// / GameProperties.getScaling(),
                0, 0,
                GameProperties.getDisplayWidth(),
                GameProperties.getDisplayHeight(),
                GL_COLOR_BUFFER_BIT, GL_NEAREST);
    }
}
