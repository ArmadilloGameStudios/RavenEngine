package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.Camera;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;
import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferFloat;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.opengl.GL32.GL_TEXTURE_2D_MULTISAMPLE;

/**
 * Created by cookedbird on 11/21/17.
 */
public class WorldWaterShader extends Shader {

    public static final int
        UV = getNextTexture();

    private int projection_location, model_location, view_location,
            texture_refract_color_location, texture_refract_depth_location,
            texture_reflect_color_location, texture_reflect_depth_location,
            water_uv_map_texture, texture_water_uv_map_location,
            water_uv_offset_location;

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

        texture_water_uv_map_location = glGetUniformLocation(getProgramHandel(), "waterUVMapTexture");

        water_uv_offset_location = glGetUniformLocation(getProgramHandel(), "waterUVOffset");

        glLinkProgram(getProgramHandel());

        water_uv_map_texture = glGenTextures();
        glActiveTexture(GL_TEXTURE0 + UV);
        glBindTexture(GL_TEXTURE_2D, water_uv_map_texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, 512, 512, 0, GL_RGB, GL_UNSIGNED_BYTE, getUVPixels());

        glGenerateMipmap(GL_TEXTURE_2D);

        glActiveTexture(GL_TEXTURE0);
    }

    private ByteBuffer getUVPixels()  {
        try {
            BufferedImage uv = ImageIO.read(new File("RavenEngine" + File.separator + "res" + File.separator + "water.jpg"));

            int[] pixels = new int[512 * 512];
            uv.getRGB(0, 0, 512, 512, pixels, 0, 512);

            ByteBuffer buffer = BufferUtils.createByteBuffer(512 * 512 * 3); //4 for RGBA, 3 for RGB

            Random r = new Random();

            for(int y = 0; y < 512; y++){
                for(int x = 0; x < 512; x++){
                    int pixel = pixels[y * 512 + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                    buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                    buffer.put((byte) (pixel & 0xFF));             // Blue component
                }
            }

            buffer.flip();

            return buffer;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
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

        glUniform1i(texture_water_uv_map_location, UV);

        long x = GameEngine.getEngine().getSystemTime();
        glUniform2f(water_uv_offset_location, x / 400000f, x / -20000f);

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
