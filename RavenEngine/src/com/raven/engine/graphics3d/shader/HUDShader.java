package com.raven.engine.graphics3d.shader;

import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDObject;
import com.raven.engine.worldobject.HUDText;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class HUDShader extends Shader {

    public static final int TEXTURE = getNextTexture();

    private int scale_location, style_location,
            color_location, offset_location,
            text_location, useText_location;

    public HUDShader() {
        super("hud_vertex.glsl", "hud_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        scale_location = glGetUniformLocation(getProgramHandel(), "scale");
        style_location = glGetUniformLocation(getProgramHandel(), "style");
        offset_location = glGetUniformLocation(getProgramHandel(), "offset");
        color_location = glGetUniformLocation(getProgramHandel(), "color");
        text_location = glGetUniformLocation(getProgramHandel(), "text");
        useText_location = glGetUniformLocation(getProgramHandel(), "useText");
    }

    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glUniform1i(text_location, HUDShader.TEXTURE);

        glDisable(GL_DEPTH_TEST);
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
    }

    @Override
    public void endProgram() {
        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }
}
