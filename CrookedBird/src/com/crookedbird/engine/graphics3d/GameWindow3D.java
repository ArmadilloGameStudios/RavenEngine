package com.crookedbird.engine.graphics3d;

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
import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_INDEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
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
import static org.lwjgl.opengl.GL13.GL_MULTISAMPLE;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.file.Files;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import com.crookedbird.engine.GameEngine;

public class GameWindow3D {

	// The window handle
	private long window;
	private int program;
	
	private GameEngine engine;

	public GameWindow3D(GameEngine engine) {
		this.engine = engine;
	}

	public void setup() {
		try {
			// Setup an error callback. The default implementation
			// will print the error message in System.err.
			GLFWErrorCallback.createPrint(System.err).set();

			// Initialize GLFW. Most GLFW functions will not work before doing
			// this.
			if (!glfwInit())
				throw new IllegalStateException("Unable to initialize GLFW");

			// Configure GLFW
			glfwDefaultWindowHints(); // optional, the current window hints are
										// already the default
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay
														// hidden
														// after creation
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be
														// resizable

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

			GL.createCapabilities();

			// Create Shaders
			// Vertex Shader
			File shaderv = new File("shaders" + File.separator + "vertex.glsl");

			int vShader = glCreateShader(GL_VERTEX_SHADER);
			glShaderSource(vShader,
					new String(Files.readAllBytes(shaderv.toPath())));
			glCompileShader(vShader);

			IntBuffer iVal = BufferUtils.createIntBuffer(1);
			glGetShaderiv(vShader, GL_COMPILE_STATUS, iVal);
			if (GL_TRUE != iVal.get() || true) {
				System.out.println("Vertex Shader Failed: "
						+ glGetShaderInfoLog(vShader));
			}

			// Frag Shader
			File shaderf = new File("shaders" + File.separator + "fragment.glsl");
			int fShader = glCreateShader(GL_FRAGMENT_SHADER);
			glShaderSource(fShader,
					new String(Files.readAllBytes(shaderf.toPath())));
			glCompileShader(fShader);

			iVal = BufferUtils.createIntBuffer(1);
			glGetShaderiv(fShader, GL_COMPILE_STATUS, iVal);
			if (GL_TRUE != iVal.get() || true) {
				System.out.println("Fragment Shader Failed: "
						+ glGetShaderInfoLog(fShader));
			}

			// Create the program
			program = glCreateProgram();
			glAttachShader(program, vShader);
			glAttachShader(program, fShader);
			
			glLinkProgram(program);

			iVal = BufferUtils.createIntBuffer(1);
			glGetProgramiv(program, GL_VALIDATE_STATUS, iVal);
			if (GL_TRUE != iVal.get() || true) {
				System.out.println("Program Failed: "
						+ glGetProgramInfoLog(program));
			}
			
			System.out.println("Shader version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));


			// Enable the vbo attributes
			glEnableClientState(GL_VERTEX_ARRAY);
			glEnableClientState(GL_NORMAL_ARRAY);
			glEnableClientState(GL_COLOR_ARRAY);
			glEnableClientState(GL_INDEX_ARRAY);
			
			// Enable the custom mode attribute
			glEnableVertexAttribArray(6);
			glBindAttribLocation(program, 6, "glow");
			
			// Link program			
			glLinkProgram(program);

			// glGetProgramiv();

			glUseProgram(program);

			// 
			glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

			glViewport(0, 0, engine.getGame().getWidth(), engine.getGame().getHeight());

			glfwWindowHint(GLFW_SAMPLES, 8);
			glEnable(GL_MULTISAMPLE);  
			
			glEnable(GL_DEPTH_TEST);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public long getWindowHandler() {
		return window;
	}
}
