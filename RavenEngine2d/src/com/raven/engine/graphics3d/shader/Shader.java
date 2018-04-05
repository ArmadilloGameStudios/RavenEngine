package com.raven.engine.graphics3d.shader;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.model.animation.Animation;
import com.raven.engine.graphics3d.model.animation.AnimationState;
import com.raven.engine.scene.Camera;
import com.raven.engine.scene.light.Light;
import com.raven.engine.util.math.Matrix4f;
import com.raven.engine.util.math.Plane;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL31.GL_UNIFORM_BUFFER;

/**
 * Created by cookedbird on 5/29/17.
 */
public abstract class Shader {

    public final static int LIGHT = 1, MATRICES = 2, ANIMATION = 3;

    private static int nextTexture = 0;

    protected static int getNextTexture() {
        nextTexture++;
        return nextTexture;
    }

    private static FloatBuffer pvBuffer = BufferUtils.createFloatBuffer(16 * 6);
    private static Plane plane = new Plane(0, 1, 0, 0);

    // has 'memory leak'
    private static Matrix4f tempMat = new Matrix4f();
    private static Matrix4f tempMat2 = new Matrix4f();

    public static void setProjectionViewMatrices(Camera camera) {
        GameEngine.getEngine().getWindow().printErrors("pre setProjectionViewMatrices: ");

        Matrix4f cameraViewMatrix = camera.getViewMatrix();

        cameraViewMatrix.toBuffer(pvBuffer);
        cameraViewMatrix.multiply(Matrix4f.reflection(plane, tempMat), tempMat2).toBuffer(pvBuffer);
        tempMat2.inverse(tempMat).toBuffer(pvBuffer);

        camera.getProjectionMatrix().toBuffer(pvBuffer);
        camera.getProjectionMatrix().inverse(tempMat).toBuffer(pvBuffer);

        camera.getInverseProjectionViewMatrix().toBuffer(pvBuffer);

        pvBuffer.flip();

        glBindBuffer(GL_UNIFORM_BUFFER, GameEngine.getEngine().getWindow().getMatricesHandel());
        glBufferSubData(GL_UNIFORM_BUFFER, 16 * 4, pvBuffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);

        GameEngine.getEngine().getWindow().printErrors("during setProjectionViewMatrices: ");
    }

    private static FloatBuffer mBuffer = BufferUtils.createFloatBuffer(16);

    public static void setModelMatrix(Matrix4f model) {
        model.toBuffer(mBuffer);
        mBuffer.flip();

        glBindBuffer(GL_UNIFORM_BUFFER, GameEngine.getEngine().getWindow().getMatricesHandel());
        glBufferSubData(GL_UNIFORM_BUFFER, 0, mBuffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public static void setLight(Light light) {
        FloatBuffer slBuffer = light.toFloatBuffer();
//        slBuffer.flip(); Assume Flipped

        glBindBuffer(GL_UNIFORM_BUFFER, GameEngine.getEngine().getWindow().getLightHandel());
        glBufferSubData(GL_UNIFORM_BUFFER, 0, slBuffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }

    public static final int MAX_BONE_COUNT = 24;
    private static FloatBuffer aBuffer = BufferUtils.createFloatBuffer(16 * MAX_BONE_COUNT);

    public static void setAnimationMatrices(AnimationState animationState) {
        isClear = false;

        animationState.toBuffer(aBuffer);
        aBuffer.rewind();

        setAnimationMatrices();
    }

    private static boolean isClear = false;
    private static final Matrix4f idMat = new Matrix4f();

    public static void clearAnimationMatrices() {
        if (!isClear) {
            isClear = true;

            for (int i = 0; i < MAX_BONE_COUNT; i++) {
                idMat.toBuffer(aBuffer);
            }

            aBuffer.rewind();

            setAnimationMatrices();
        }
    }

    private static void setAnimationMatrices() {
        glBindBuffer(GL_UNIFORM_BUFFER, GameEngine.getEngine().getWindow().getAnimationHandel());
        glBufferSubData(GL_UNIFORM_BUFFER, 0, aBuffer);
        glBindBuffer(GL_UNIFORM_BUFFER, 0);
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
        GameEngine.getEngine().getWindow().printErrors("Cat: 1");
        glDeleteProgram(program_handel);
        GameEngine.getEngine().getWindow().printErrors("Cat: 2");
        glDeleteShader(fragment_handel);
        GameEngine.getEngine().getWindow().printErrors("Cat: 3");
        glDeleteShader(vertex_handel);
        GameEngine.getEngine().getWindow().printErrors("Cat: 4");
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
