package com.raven.engine2d.graphics2d.shader;

import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimation;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.util.math.Vector2f;
import org.lwjgl.opengl.GL11;
import sun.java2d.pipe.SpanClipRenderer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class MainShader extends Shader {

    private GameWindow window;

    private int sprite_sheet_location, rect_location;

    public MainShader(GameWindow window) {
        super("vertex.glsl", "terrain_fragment.glsl");

        this.window = window;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        sprite_sheet_location = glGetUniformLocation(getProgramHandel(), "spriteSheet");
        rect_location = glGetUniformLocation(getProgramHandel(), "rect");
    }

    @Override
    public void useProgram() {
        super.useProgram();

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    public void draw(SpriteSheet sheet, SpriteAnimation spriteAnimation, Vector2f position, DrawStyle style) {

        switch (style) {
            case ISOMETRIC:
                drawIsometric(sheet, spriteAnimation, position, new Vector2f(10f, 10f));
                break;
        }
    }

    private float isoHeight = 17, isoWidth = 32;

    private void drawIsometric(SpriteSheet sheet, SpriteAnimation spriteAnimation, Vector2f position, Vector2f offset) {
        glUniform1i(sprite_sheet_location, sheet.getTextureActiveLocation());

        float x = position.x * isoWidth - position.y * isoWidth + offset.x;
        float y = position.x * isoHeight + position.y * isoHeight + offset.y;

        glViewport((int)x, (int)y, sheet.width * 2, sheet.height * 2);

        glUniform4f(rect_location,
                spriteAnimation.x, spriteAnimation.y,
                1, 1);

        window.drawQuad();
    }

    @Override
    public void endProgram() {

    }

    public int getWorldObjectID() {
        return 0;
    }
}
