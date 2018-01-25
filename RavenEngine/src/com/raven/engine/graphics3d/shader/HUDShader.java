package com.raven.engine.graphics3d.shader;

import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.HUDObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class HUDShader extends Shader {

    private int scale_location, style_location, color_location, offset_location;

    public HUDShader() {
        super("hud_vertex.glsl", "hud_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        scale_location = glGetUniformLocation(getProgramHandel(), "scale");
        style_location = glGetUniformLocation(getProgramHandel(), "style");
        offset_location = glGetUniformLocation(getProgramHandel(), "offset");
        color_location = glGetUniformLocation(getProgramHandel(), "color");
    }

    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glDisable(GL_DEPTH_TEST);
    }

    public void setProperties(HUDObject o) {
        glUniform2f(scale_location, o.getWidth(), o.getHeight());
        glUniform2f(offset_location, o.getXOffset(), o.getYOffset());
        glUniform1i(style_location, o.getStyle());

        Vector4f color = o.getColor();
        glUniform4f(color_location, color.x, color.y, color.z, color.w);

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
