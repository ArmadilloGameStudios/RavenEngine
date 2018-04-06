package com.raven.engine2d.graphics2d.shader;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine.graphics3d.model.animation.AnimationState;
import com.raven.engine2d.scene.Camera;
import com.raven.engine2d.scene.light.Light;
import com.raven.engine2d.util.math.Matrix4f;
import com.raven.engine2d.util.math.Plane;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 * Created by cookedbird on 5/29/17.
 */
public abstract class Shader {

    public final static int LIGHT = 1, MATRICES = 2, ANIMATION = 3;
    public static final int TEXTURE = getNextTexture();

    private static int nextTexture = 0;

    protected static int getNextTexture() {
        nextTexture++;
        return nextTexture;
    }

    int vertex_handel, fragment_handel, program_handel;

    public Shader(String vertex_shader, String fragment_shader) {
        try {
            // Get the variable map
            Map<String, String> variables = getGLSLVariableMap();

            // Create Shaders
            // Vertex Shader
            File shaderv = new File("RavenEngine" + File.separator + "shaders" + File.separator
                    + vertex_shader);
            vertex_handel = glCreateShader(GL_VERTEX_SHADER);

            List<String> vertex_file_lines = Files.readAllLines(shaderv.toPath());

            if (variables != null)
                for (String key : variables.keySet()) {
                    for (int i = 0; i < vertex_file_lines.size(); i++) {
                        if (vertex_file_lines.get(i).contains(key)) {
                            String line = "#define " + key + " " + variables.get(key);
                            vertex_file_lines.set(i, line);
                            break;
                        }
                    }
                }

            String vertex_file_string = String.join("\n", vertex_file_lines);

            glShaderSource(vertex_handel, vertex_file_string);
            glCompileShader(vertex_handel);

            IntBuffer iVal = BufferUtils.createIntBuffer(1);
            glGetShaderiv(vertex_handel, GL_COMPILE_STATUS, iVal);
            if (GL_TRUE != iVal.get()) {
                System.out.println("Vertex Shader Failed: " + vertex_shader + "\n"
                        + glGetShaderInfoLog(vertex_handel));
            }

            // Frag Shader
            File shaderf = new File("RavenEngine" + File.separator + "shaders" + File.separator
                    + fragment_shader);
            fragment_handel = glCreateShader(GL_FRAGMENT_SHADER);

            List<String> fragment_file_lines = Files.readAllLines(shaderf.toPath());

            if (variables != null)
                for (String key : variables.keySet()) {
                    for (int i = 0; i < fragment_file_lines.size(); i++) {
                        if (fragment_file_lines.get(i).contains(key)) {
                            String line = "#define " + key + " " + variables.get(key);
                            fragment_file_lines.set(i, line);
                            break;
                        }
                    }
                }

            String fragment_file_string = String.join("\n", fragment_file_lines);

            glShaderSource(fragment_handel, fragment_file_string);
            glCompileShader(fragment_handel);

            iVal = BufferUtils.createIntBuffer(1);
            glGetShaderiv(fragment_handel, GL_COMPILE_STATUS, iVal);
            if (GL_TRUE != iVal.get()) {
                System.out.println("Fragment Shader Failed: " + fragment_shader + "\n"
                        + glGetShaderInfoLog(fragment_handel));
            }

            // Create the program
            program_handel = glCreateProgram();
            glAttachShader(program_handel, vertex_handel);
            glAttachShader(program_handel, fragment_handel);

            glLinkProgram(program_handel);

            iVal = BufferUtils.createIntBuffer(1);
            glGetProgramiv(program_handel, GL_VALIDATE_STATUS, iVal);
            if (GL_TRUE != iVal.get()) {
                System.out.println("Program Failed: "
                        + glGetProgramInfoLog(program_handel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void useProgram() {
        if (GameEngine.getEngine().getWindow().getActiveShader() != this) {
            GameEngine.getEngine().getWindow().endActiveShader();
            GameEngine.getEngine().getWindow().setActiveShader(this);
        }
        glUseProgram(getProgramHandel());
    }

    public abstract void endProgram();

    protected final int getVertexHandel() {
        return vertex_handel;
    }

    protected final int getFragmentHandel() {
        return fragment_handel;
    }

    public final int getProgramHandel() {
        return program_handel;
    }

    protected final void releaseProgram() {
        glDeleteProgram(program_handel);
        glDeleteShader(fragment_handel);
        glDeleteShader(vertex_handel);
    }

    private final Map<String, String> getGLSLVariableMap() {
        Map<String, String> map = new HashMap<>();

        map.put("NUM_SAMPLES", Integer.toString(GameProperties.getMultisampleCount()));

        return map;
    }

    public void release() {
        releaseProgram();
    }
}
