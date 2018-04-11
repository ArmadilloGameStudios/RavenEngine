package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.util.math.Vector4f;
import com.raven.engine.worldobject.HUDImage;
import com.raven.engine.worldobject.HUDObject;
import com.raven.engine.worldobject.HUDText;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT1;

public class HUDMSShader extends Shader {

    public static final int TEXTURE = getNextTexture();

    private int scale_location, style_location,
            color_location, offset_location,
            text_location, useText_location,
            id_location;

    public HUDMSShader() {
        super("hud_vertex.glsl", "hud_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        scale_location = glGetUniformLocation(getProgramHandel(), "scale");
        style_location = glGetUniformLocation(getProgramHandel(), "style");
        offset_location = glGetUniformLocation(getProgramHandel(), "offset");
        color_location = glGetUniformLocation(getProgramHandel(), "color");
        text_location = glGetUniformLocation(getProgramHandel(), "text");
        useText_location = glGetUniformLocation(getProgramHandel(), "useText");
        id_location = glGetUniformLocation(getProgramHandel(), "id");
    }

    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());
        glDrawBuffers(new int[] { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1 });

        glUniform1i(text_location, UIShader.TEXTURE);

        glDisable(GL_DEPTH_TEST);

        // Blend
        glEnable(GL_BLEND);
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public void setProperties(HUDObject o) {
        glUniform2f(scale_location, o.getWidth(), o.getHeight());
        glUniform2f(offset_location, o.getXOffset(), o.getYOffset());
        glUniform1i(style_location, o.getStyle());

        glUniform1i(useText_location, 0);

        Vector4f color = o.getColor();
        glUniform4f(color_location, color.x, color.y, color.z, color.w);

        if (o.doBlend()) {
            glEnable(GL_BLEND);
        } else {
            glDisable(GL_BLEND);
        }

        // id
        int id = o.getID();

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            if (id != 0) {
                int r = (id & 0x000000FF) >> 0;
                int g = (id & 0x0000FF00) >> 8;
                int b = (id & 0x00FF0000) >> 16;

                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
            }

    }

    public void setProperties(HUDText o) {
        glUniform2f(scale_location, o.getWidth(), o.getHeight());
        glUniform2f(offset_location, o.getXOffset(), o.getYOffset());
        glUniform1i(style_location, o.getStyle());

        glUniform1i(useText_location, 1);

        if (o.doBlend()) {
            glEnable(GL_BLEND);
        } else {
            glDisable(GL_BLEND);
        }

        // id
        int id = o.getID();

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            if (id != 0) {
                int r = (id & 0x000000FF) >> 0;
                int g = (id & 0x0000FF00) >> 8;
                int b = (id & 0x00FF0000) >> 16;

                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
            }
    }

    public void setProperties(HUDImage o) {
        glUniform2f(scale_location, o.getWidth(), o.getHeight());
        glUniform2f(offset_location, o.getXOffset(), o.getYOffset());
        glUniform1i(style_location, o.getStyle());

        glUniform1i(useText_location, 1);

        if (o.doBlend()) {
            glEnable(GL_BLEND);
        } else {
            glDisable(GL_BLEND);
        }

        // id
        int id = o.getID();

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            if (id != 0) {
                int r = (id & 0x000000FF) >> 0;
                int g = (id & 0x0000FF00) >> 8;
                int b = (id & 0x00FF0000) >> 16;

                glUniform3f(id_location, r / 255.0f, g / 255.0f, b / 255.0f);
            }
    }

    @Override
    public void endProgram() {
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
