package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.Camera;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Created by cookedbird on 11/21/17.
 */
public class WorldWaterShader extends Shader {
    private int projection_location, model_location, view_location,
            texture_refract_color_location, texture_refract_depth_location,
            texture_reflect_color_location, texture_reflect_depth_location;

    private Matrix4f projection_matrix = new Matrix4f(),
            model_matrix = new Matrix4f(),
            view_matrix = new Matrix4f();

    private WorldShader worldShader;

    public WorldWaterShader(WorldShader worldShader) {
        super("world_water_vertex.glsl", "world_water_fragment.glsl");

        this.worldShader = worldShader;

        glBindAttribLocation(getProgramHandel(), 0, "vertex_pos");
        glBindAttribLocation(getProgramHandel(), 1, "vertex_color");
        glBindAttribLocation(getProgramHandel(), 2, "vertex_normal");

        projection_location = glGetUniformLocation(getProgramHandel(), "P");
        model_location = glGetUniformLocation(getProgramHandel(), "M");
        view_location = glGetUniformLocation(getProgramHandel(), "V");

        texture_refract_color_location = glGetUniformLocation(getProgramHandel(), "refractColorTexture");
        texture_refract_depth_location = glGetUniformLocation(getProgramHandel(), "refractDepthTexture");

        texture_reflect_color_location = glGetUniformLocation(getProgramHandel(), "reflectColorTexture");
        texture_reflect_depth_location = glGetUniformLocation(getProgramHandel(), "reflectDepthTexture");

        glLinkProgram(getProgramHandel());
    }


    public void useProgram(Camera camera) {
        super.useProgram();

        glUseProgram(getProgramHandel());

        glBindFramebuffer(GL_FRAMEBUFFER, worldShader.getFrameBuffer());

        glViewport(0, 0,
                GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight());

        glUniform1i(texture_refract_color_location, WaterRefractionShader.COLOR);
        glUniform1i(texture_refract_depth_location, WaterRefractionShader.DEPTH);

        glUniform1i(texture_reflect_color_location, WaterReflectionShader.COLOR);
        glUniform1i(texture_reflect_depth_location, WaterReflectionShader.DEPTH);

        // Enable the custom mode attribute
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glUniformMatrix4fv(projection_location, false, projection_matrix.toBuffer());
        glUniformMatrix4fv(model_location, false, model_matrix.toBuffer());
        glUniformMatrix4fv(view_location, false, view_matrix.toBuffer());
    }

    @Override
    public void endProgram() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
    }

    public void setProjectionMatrix(Matrix4f m) {
        projection_matrix = m;

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(projection_location, false, projection_matrix.toBuffer());
    }

    public void setViewMatrix(Matrix4f m) {
        view_matrix = m;

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(view_location, false, view_matrix.toBuffer());
    }

    public void setModelMatrix(Matrix4f m) {
        model_matrix = m;

        if (GameEngine.getEngine().getWindow().getActiveShader() == this)
            glUniformMatrix4fv(model_location, false, model_matrix.toBuffer());
    }
}
