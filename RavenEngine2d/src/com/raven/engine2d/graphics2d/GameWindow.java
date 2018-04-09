package com.raven.engine2d.graphics2d;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.shader.MainShader;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.opengl.GL45.GL_CONTEXT_LOST;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow {

    // The window handle
    private long window;

    private MainShader mainShader;

    private int sun_light_buffer_handel, matrices_buffer_handel, animation_buffer_handel;

    private GameEngine engine;

    private Map<Integer, Boolean> keyboard = new HashMap<>();
    private Map<Integer, Boolean> mouse = new HashMap<>();
    private Shader activeShader;

    public GameWindow(GameEngine engine) {
        this.engine = engine;
    }

    public void create() {
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

        // Create the window
        window = glfwCreateWindow(GameProperties.getScreenWidth(),
                GameProperties.getScreenHeight(),
                engine.getGame().getTitle(), glfwGetPrimaryMonitor(),
                NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

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

        mainShader = new MainShader(this);

        // Enable depth test
        glEnable(GL_DEPTH_TEST);

        // Enable face culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Setup ScreenQuad
        ScreenQuad.loadQuad();
    }

    public void destroy() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();

        System.out.println("Window Destroyed");
    }

    public long getWindowHandler() {
        return window;
    }

    public MainShader getMainShader() {
        return mainShader;
    }

    public int getLightHandel() {
        return sun_light_buffer_handel;
    }

    public int getMatricesHandel() {
        return matrices_buffer_handel;
    }

    public int getAnimationHandel() {
        return animation_buffer_handel;
    }

    public void drawQuad() {
        // Draw FBO
        glEnableVertexAttribArray(0);
        ScreenQuad.getBlankModel().draw();
        glDisableVertexAttribArray(0);
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
            switch (err) {
                case GL_INVALID_ENUM:
                    System.out.println(tag + " GL_INVALID_ENUM 0x" + Integer.toHexString(err));
                    break;
                case GL_INVALID_VALUE:
                    System.out.println(tag + " GL_INVALID_VALUE 0x" + Integer.toHexString(err));
                    break;
                case GL_INVALID_OPERATION:
                    System.out.println(tag + " GL_INVALID_OPERATION 0x" + Integer.toHexString(err));
                    break;
                case GL_STACK_OVERFLOW:
                    System.out.println(tag + " GL_STACK_OVERFLOW 0x" + Integer.toHexString(err));
                    break;
                case GL_OUT_OF_MEMORY:
                    System.out.println(tag + " GL_OUT_OF_MEMORY 0x" + Integer.toHexString(err));
                    break;
                case GL_INVALID_FRAMEBUFFER_OPERATION:
                    System.out.println(tag + " GL_INVALID_FRAMEBUFFER_OPERATION 0x" + Integer.toHexString(err));
                    break;
                case GL_CONTEXT_LOST:
                    System.out.println(tag + " GL_CONTEXT_LOST 0x" + Integer.toHexString(err));
                    break;
                case GL_TABLE_TOO_LARGE:
                    System.out.println(tag + " GL_TABLE_TOO_LARGE 0x" + Integer.toHexString(err));
                    break;
                default:
                    System.out.println(tag + " 0x" + Integer.toHexString(err));
                    break;
            }
        }
    }

    public Shader getActiveShader() {
        return activeShader;
    }

    public void setActiveShader(Shader activeShader) {
        this.activeShader = activeShader;
    }
}
