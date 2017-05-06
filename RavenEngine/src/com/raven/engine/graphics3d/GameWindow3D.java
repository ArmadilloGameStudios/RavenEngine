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
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL45.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL45.glTextureSubImage2D;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private int mainProgram, drawFBOProgram, bloomProgram, fbo, fboColorTexture,
            fboGlowTexture, fboIDTexture, fboRenderBuffer, fbohor, fbohorBloomTexture, fbohorRenderBuffer;

    private int uProjectionMatrix, uViewMatrix, uModelMatrix;
    private int uTextureColorfbo, uTextureGlowfbo, uID, uBloomStepfbo, uBloomStephor, uTextureGlowhor;

    private GameEngine engine;

    private Map<Integer, Boolean> keyboard = new HashMap<Integer, Boolean>();
    private Map<Integer, Boolean> mouse = new HashMap<Integer, Boolean>();

    public GameWindow3D(GameEngine engine) {
        this.engine = engine;
    }

    public void setup() {
        try {
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
            // main
            mainProgram = loadProgram("vertex.glsl", "fragment.glsl");

            glBindAttribLocation(mainProgram, 0, "vertex_pos");
            glBindAttribLocation(mainProgram, 1, "vertex_color");
            glBindAttribLocation(mainProgram, 2, "vertex_normal");
            glBindAttribLocation(mainProgram, 3, "vertex_glow");


            uProjectionMatrix = glGetUniformLocation(mainProgram, "P");
            uModelMatrix = glGetUniformLocation(mainProgram, "M");
            uViewMatrix = glGetUniformLocation(mainProgram, "V");

            uID = glGetUniformLocation(mainProgram, "id");

            glLinkProgram(mainProgram);

            // bloom
			bloomProgram = loadProgram("vertex2.glsl", "bloomf.glsl");

            uTextureGlowhor = glGetUniformLocation(bloomProgram, "glowTexture");
			uBloomStephor = glGetUniformLocation(bloomProgram, "bloomStep");

			glLinkProgram(bloomProgram);

            // combine
            drawFBOProgram = loadProgram("vertex2.glsl", "fragment2.glsl");

            uTextureColorfbo = glGetUniformLocation(drawFBOProgram, "colorTexture");
            uTextureGlowfbo = glGetUniformLocation(drawFBOProgram, "glowTexture");
            uBloomStepfbo = glGetUniformLocation(drawFBOProgram, "bloomStep");

            glLinkProgram(drawFBOProgram);

            // Create FBOs
            setupFBO();
            setupBloomHorFBO();

            // Enable multisample
            // glfwWindowHint(GLFW_SAMPLES, 8);
            // glEnable(GL_MULTISAMPLE);

            glEnable(GL_DEPTH_TEST);

            // Enable face culling
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);

            // Setup ModelReference Error
            ModelReference.setBlankModel();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private int loadProgram(String v, String f) throws IOException {
        int program = 0;

        // Create Shaders
        // Vertex Shader
        File shaderv = new File("RavenEngine" + File.separator + "shaders" + File.separator
                + v);
        int vShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vShader,
                new String(Files.readAllBytes(shaderv.toPath())));
        glCompileShader(vShader);

        IntBuffer iVal = BufferUtils.createIntBuffer(1);
        glGetShaderiv(vShader, GL_COMPILE_STATUS, iVal);
        if (GL_TRUE != iVal.get()) {
            System.out.println("Vertex Shader Failed: " + v + "\n"
                    + glGetShaderInfoLog(vShader));
        }

        // Frag Shader
        File shaderf = new File("RavenEngine" + File.separator + "shaders" + File.separator
                + f);
        int fShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fShader,
                new String(Files.readAllBytes(shaderf.toPath())));
        glCompileShader(fShader);

        iVal = BufferUtils.createIntBuffer(1);
        glGetShaderiv(fShader, GL_COMPILE_STATUS, iVal);
        if (GL_TRUE != iVal.get()) {
            System.out.println("Fragment Shader Failed: " + f + "\n"
                    + glGetShaderInfoLog(fShader));
        }

        // Create the program
        program = glCreateProgram();
        glAttachShader(program, vShader);
        glAttachShader(program, fShader);

        glLinkProgram(program);

        iVal = BufferUtils.createIntBuffer(1);
        glGetProgramiv(program, GL_VALIDATE_STATUS, iVal);
        if (GL_TRUE != iVal.get()) {
            System.out.println("Program Failed: "
                    + glGetProgramInfoLog(program));
        }

        return program;
    }

    private void setupFBO() {
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // FBO Textures
        // Color
        fboColorTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fboColorTexture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, engine.getGame().getWidth(),
                engine.getGame().getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                fboColorTexture, 0);

        // Glow
        fboGlowTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fboGlowTexture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, engine.getGame().getWidth(),
                engine.getGame().getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT1,
                fboGlowTexture, 0);

        // ID
        fboIDTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fboIDTexture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, engine.getGame().getWidth(),
                engine.getGame().getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT2,
                fboIDTexture, 0);

        // Draw buffers
        IntBuffer fboBuffers = BufferUtils.createIntBuffer(3);
        int bfs[] = {GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1,
                GL_COLOR_ATTACHMENT2};
        for (int i = 0; i < bfs.length; i++)
            fboBuffers.put(bfs[i]);
        fboBuffers.flip();

        glDrawBuffers(fboBuffers);

        // Depth
        fboRenderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, fboRenderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, engine
                .getGame().getWidth(), engine.getGame().getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                GL_RENDERBUFFER, fboRenderBuffer);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("FBO Failed: "
                    + glCheckFramebufferStatus(GL_FRAMEBUFFER));
        }
    }

    private void setupBloomHorFBO() {
        fbohor = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbohor);

        // FBO Textures
        // Color
        fbohorBloomTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, fbohorBloomTexture);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, engine.getGame().getWidth(),
                engine.getGame().getHeight(), 0, GL_RGB, GL_UNSIGNED_BYTE, 0);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                fbohorBloomTexture, 0);

        // Draw buffers
        IntBuffer fboBuffers = BufferUtils.createIntBuffer(3);
        int bfs[] = { GL_COLOR_ATTACHMENT0 };
        for (int i = 0; i < bfs.length; i++)
            fboBuffers.put(bfs[i]);
        fboBuffers.flip();

        glDrawBuffers(fboBuffers);

        // Depth
        fbohorRenderBuffer = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, fbohorRenderBuffer);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, engine
                .getGame().getWidth(), engine.getGame().getHeight());
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
                GL_RENDERBUFFER, fbohorRenderBuffer);

        // Errors
        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            System.out.println("FBOHOR Failed: "
                    + glCheckFramebufferStatus(GL_FRAMEBUFFER));
        }
    }

    public long getWindowHandler() {
        return window;
    }

    public void setProgramMain() {
        glUseProgram(mainProgram);
    }

    public void setProgramBloomHorizontal() {
        glUseProgram(bloomProgram);

        // Bind the glow
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, fboGlowTexture);
        glUniform1i(uTextureGlowhor, 3);

        glUniform2f(uBloomStephor, 1f / engine.getGame().getWidth(), 0f);
    }

    public void setProgramDrawFBO() {
        glUseProgram(drawFBOProgram);

        // Bind the color
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, fboColorTexture);
        glUniform1i(uTextureColorfbo, 0);

        // Bind the glow
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, fbohorBloomTexture);
        glUniform1i(uTextureGlowfbo, 1);

        glUniform2f(uBloomStepfbo, 0f, 1f / engine.getGame().getHeight());
    }

    public void setRenderTargetFBO() {
        setRenderTargetFBO(false);
    }

    public void setRenderTargetFBO(boolean clear) {
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        glViewport(0, 0, engine.getGame().getWidth(), engine.getGame().getHeight());

        if (clear) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }
    }

    public void setRenderTargetFBOHOR() {
        setRenderTargetFBOHOR(false);
    }

    public void setRenderTargetFBOHOR(boolean clear) {
        glBindFramebuffer(GL_FRAMEBUFFER, fbohor);

        glViewport(0, 0, engine.getGame().getWidth(), engine.getGame().getHeight());

        if (clear) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }
    }

    public void setRenderTargetWindow() {
        setRenderTargetFBO(false);
    }

    public void setRenderTargetWindow(boolean clear) {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glViewport(0, 0, engine.getGame().getWidth(), engine.getGame()
                .getHeight());

        if (clear) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void drawFBO() {
        // Draw FBO
        glEnableVertexAttribArray(0);
        ModelReference.getBlankModel().draw();
        glDisableVertexAttribArray(0);
    }

    public void setProjectionMatrix(Matrix4f m) {
        glUniformMatrix4fv(uProjectionMatrix, false, m.toBuffer());
    }

    public void setViewMatrix(Matrix4f m) {
        glUniformMatrix4fv(uViewMatrix, false, m.toBuffer());
    }

    public void setModelMatrix(Matrix4f m) {
        glUniformMatrix4fv(uModelMatrix, false, m.toBuffer());
    }

    public void setWorldObjectID(int id) {
        int r = (id & 0x000000FF) >> 0;
        int g = (id & 0x0000FF00) >> 8;
        int b = (id & 0x00FF0000) >> 16;

        glUniform3f(uID, r / 255.0f, g / 255.0f, b / 255.0f);
    }

    IntBuffer pixelreadBuffer = BufferUtils.createIntBuffer(1);
    DoubleBuffer coursorXPosBuffer = BufferUtils.createDoubleBuffer(1);
    DoubleBuffer coursorYPosBuffer = BufferUtils.createDoubleBuffer(1);
    public int getWorldObjectID() {
        glfwGetCursorPos(window, coursorXPosBuffer, coursorYPosBuffer);

        glFlush();
        glFinish();

        glBindFramebuffer(GL_FRAMEBUFFER, fbo);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glReadBuffer(GL_COLOR_ATTACHMENT2);
        glReadPixels((int)coursorXPosBuffer.get(), engine.getGame().getHeight() - (int)coursorYPosBuffer.get(),
                1, 1, GL_RGB, GL_UNSIGNED_BYTE, pixelreadBuffer);

        int id = pixelreadBuffer.get();

        pixelreadBuffer.flip();
        coursorXPosBuffer.flip();
        coursorYPosBuffer.flip();

        return id;
    }

    public void processInput(List<WorldObject> mouseOverObjects) {
        // get all the events
        glfwPollEvents();

        if (mouse.get(GLFW_MOUSE_BUTTON_1) != null && mouse.get(GLFW_MOUSE_BUTTON_1))
            for (WorldObject obj : mouseOverObjects) {
                obj.onMouseClick();
            }
    }
}
