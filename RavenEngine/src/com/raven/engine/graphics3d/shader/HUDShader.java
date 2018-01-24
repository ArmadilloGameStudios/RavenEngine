package com.raven.engine.graphics3d.shader;

import com.raven.engine.worldobject.HUDObject;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.*;

public class HUDShader extends Shader {

    private int scale_location;

    public HUDShader() {
        super("hud_vertex.glsl", "hud_fragment.glsl");

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");

        scale_location = glGetUniformLocation(getProgramHandel(), "scale");
    }

    public void useProgram() {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glDisable(GL_DEPTH_TEST);
    }

    public void setProperties(HUDObject o) {
        glUniform2f(scale_location, o.getScaleX(), o.getScaleY());
    }

    @Override
    public void endProgram() {
        glEnable(GL_DEPTH_TEST);
    }
}
