package com.raven.engine2d.graphics2d.shader;

import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimation;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.opengl.GL11;
import sun.java2d.pipe.SpanClipRenderer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.*;
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

        glEnable(GL_BLEND);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void endProgram() {
        glDisable(GL_BLEND);
    }

    public void draw(SpriteSheet sheet, SpriteAnimationState spriteAnimation, Vector2f position, Vector2f offset, DrawStyle style) {

        switch (style) {
            case ISOMETRIC:
                drawIsometric(sheet, spriteAnimation, position, offset);
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


    public int getWorldObjectID() {
        return 0;
    }

    public void clear(Vector3f backgroundColor) {
        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glClearColor(backgroundColor.x, backgroundColor.y, backgroundColor.z, 1f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
