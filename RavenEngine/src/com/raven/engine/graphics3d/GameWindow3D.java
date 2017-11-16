package com.raven.engine.graphics3d;

import com.raven.engine.GameEngine;
import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.shader.*;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow3D {

    // The window handle
    private long window;

    private int ms_count = 4;

    private WorldShader worldShader;
    private WaterShader waterShader;
    private BloomShader bloomShader;
    private CombinationShader combinationShader;

    private GameEngine engine;

    private Map<Integer, Boolean> keyboard = new HashMap<>();
    private Map<Integer, Boolean> mouse = new HashMap<>();
    private Shader activeShader;

    public GameWindow3D(GameEngine engine) {
        this.engine = engine;
    }

    public void setup() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, ms_count);

        // Create the window
        window = glfwCreateWindow(GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                engine.getGame().getTitle(), NULL, // glfwGetPrimaryMonitor(),
                NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);

        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        // Key and Mouse input
        glfwSetInputMode(window, GLFW_STICKY_MOUSE_BUTTONS, 1);
        glfwSetInputMode(window, GLFW_STICKY_KEYS, 1);
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> engine.inputKey(key, action, mods));
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> engine.inputMouseButton(button, action, mods));
        glfwSetCursorPosCallback(window, (window, xpos, ypos) -> engine.inputMouseMove(xpos, ypos));
        glfwSetScrollCallback(window, (window, xoffset, yoffset) -> engine.inputScroll(xoffset, yoffset));

        GL.createCapabilities();

        // Shaders
        worldShader = new WorldShader();
        waterShader = new WaterShader();
        bloomShader = new BloomShader();
        combinationShader = new CombinationShader();

        // Enable multisample
        glEnable(GL_MULTISAMPLE);

        // Enable depth test
        glEnable(GL_DEPTH_TEST);

        // Enable face culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Setup ModelReference Error
        ModelReference.loadBlankModel();
    }

    public long getWindowHandler() {
        return window;
    }

    public WorldShader getWorldShader() {
        return worldShader;
    }

    public BloomShader getBloomShader() {
        return bloomShader;
    }

    public WaterShader getWaterShader() {
        return waterShader;
    }

    public CombinationShader getCombinationShader() {
        return combinationShader;
    }

    public void drawFBO() {
        // Draw FBO
        glEnableVertexAttribArray(0);
        ModelReference.getBlankModel().draw();
        glDisableVertexAttribArray(0);
    }

    public int getMultisampleCount() {
        return ms_count;
    }

    public void setActiveShader(Shader activeShader) {
        this.activeShader = activeShader;
    }

    public void endActiveShader() {
        if (activeShader != null)
            activeShader.endProgram();
    }

    public void printErrors() {
        printErrors("");
    }

    public void printErrors(String tag) {
        int err;
        while ((err = glGetError()) != GL_NO_ERROR) {
            System.out.println(tag + "0x" + Integer.toHexString(err));
        }
    }

    public Shader getActiveShader() {
        return activeShader;
    }

}
