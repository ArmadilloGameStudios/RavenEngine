package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

/**
 * Created by cookedbird on 5/29/17.
 */
public abstract class Shader {
    int vertex_handel, fragment_handel, program_handel;

    public Shader (String vertex_shader, String fragment_shader) {
        try {
            // Create Shaders
            // Vertex Shader
            File shaderv = new File("RavenEngine" + File.separator + "shaders" + File.separator
                    + vertex_shader);
            vertex_handel = glCreateShader(GL_VERTEX_SHADER);
            glShaderSource(vertex_handel,
                    new String(Files.readAllBytes(shaderv.toPath())));
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
            glShaderSource(fragment_handel,
                    new String(Files.readAllBytes(shaderf.toPath())));
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

    public void useProgram() {
        if (GameEngine.getEngine().getWindow().getActiveShader() != this) {
            GameEngine.getEngine().getWindow().endActiveShader();
            GameEngine.getEngine().getWindow().setActiveShader(this);
        }
    }
    public abstract void endProgram();

    protected final int getVertexHandel() {
        return vertex_handel;
    }

    protected final int getFragmentHandel() {
        return fragment_handel;
    }

    protected final int getProgramHandel() {
        return program_handel;
    }
}
