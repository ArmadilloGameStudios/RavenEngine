package com.raven.engine.graphics3d;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL45.glNamedFramebufferDrawBuffers;
import static org.lwjgl.opengl.GL45.glTextureSubImage2D;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raven.engine.graphics3d.shader.BloomShader;
import com.raven.engine.graphics3d.shader.CombinationShader;
import com.raven.engine.graphics3d.shader.Shader;
import com.raven.engine.graphics3d.shader.WorldShader;
import com.raven.engine.worldobject.WorldObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import com.raven.engine.GameEngine;
import com.raven.engine.util.Matrix4f;

public class GameWindow3D {

    // The window handle
    private long window;

    private int ms_count = 4;

    private WorldShader worldShader;
    private BloomShader bloomShader;
    private CombinationShader combinationShader;

    private GameEngine engine;

    private Map<Integer, Boolean> keyboard = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mouse = new HashMap<Integer, Boolean>();
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
        // glfwWindowHint(GLFW_SAMPLES, 8);

        // Create the window
        window = glfwCreateWindow(engine.getGame().getWidth(), engine
                        .getGame().getHeight(), engine.getGame().getTitle(), NULL,
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
        glfwSetInputMode(window, GLFW_STICKY_MOUSE_BUTTONS, 1);
        glfwSetInputMode(window, GLFW_STICKY_KEYS, 1);
        glfwSetKeyCallback(window, new GLFWKeyCallbackI() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                keyboard.put(key, action == GLFW_KEY_DOWN);
            }
        });
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallbackI() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                mouse.put(button, action == GLFW_PRESS);
            }
        });

        GL.createCapabilities();

        // Shaders
        worldShader = new WorldShader();
        bloomShader = new BloomShader(worldShader.getBloomTexture());
        combinationShader = new CombinationShader(worldShader.getColorTexture(),
                bloomShader.getBloomTexture());

        // Enable multisample
        glEnable(GL_MULTISAMPLE);

        glEnable(GL_DEPTH_TEST);

        // Enable face culling
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Setup ModelReference Error
        ModelReference.setBlankModel();
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

    public CombinationShader getCombinationShader() {
        return combinationShader;
    }

    public void drawFBO() {
        // Draw FBO
        glEnableVertexAttribArray(0);
        ModelReference.getBlankModel().draw();
        glDisableVertexAttribArray(0);
    }

    public void processInput(List<WorldObject> mouseOverObjects) {
        // get all the events
        glfwPollEvents();

        if (mouse.get(GLFW_MOUSE_BUTTON_1) != null && mouse.get(GLFW_MOUSE_BUTTON_1))
            for (WorldObject obj : mouseOverObjects) {
                obj.onMouseClick();
            }
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

    public void getErrors() {
        getErrors("");
    }

    public void getErrors(String tag) {
        int err;
        if ((err = glGetError()) != GL_NO_ERROR) {
            System.out.println(tag + "0x" + Integer.toHexString(err));
        }
    }

    public Shader getActiveShader() {
        return activeShader;
    }
}
