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
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_INDEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
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
import static org.lwjgl.opengl.GL13.*;
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
import com.crookedbird.engine.util.Matrix4f;

public class GameWindow3D {

	// The window handle
	private long window;

	private int mainProgram, drawFBOProgram, fbo, fboColorTexture,
			fboGlowTexture, fboIDTexture, fboRenderBuffer;
	
	private int uProjectionMatrix, uViewMatrix, uModelMatrix;
	private int uTextureColor, uTextureGlow, uTextureID;

	private GameEngine engine;

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

			GL.createCapabilities();

			// Shaders
			mainProgram = loadProgram("shaders" + File.separator
					+ "vertex.glsl", "shaders" + File.separator
					+ "fragment.glsl");

			glBindAttribLocation(mainProgram, 0, "vertex_pos");
			glBindAttribLocation(mainProgram, 1, "vertex_color");
			glBindAttribLocation(mainProgram, 2, "vertex_normal");
			glBindAttribLocation(mainProgram, 3, "vertex_glow");

			uProjectionMatrix = glGetUniformLocation(mainProgram, "P");
			uModelMatrix = glGetUniformLocation(mainProgram, "M");
			uViewMatrix = glGetUniformLocation(mainProgram, "V");

			glLinkProgram(mainProgram);
			
			drawFBOProgram = loadProgram("shaders" + File.separator
					+ "vertex2.glsl", "shaders" + File.separator
					+ "fragment2.glsl");

			uTextureColor = glGetUniformLocation(drawFBOProgram, "colorTexture");
			uTextureGlow = glGetUniformLocation(drawFBOProgram, "glowTexture");
			uTextureID = glGetUniformLocation(drawFBOProgram, "idTexture");
			
			glLinkProgram(drawFBOProgram);

			// Create FBOs
			setupFBO();

			// Enable multisample
			// glfwWindowHint(GLFW_SAMPLES, 8);
			// glEnable(GL_MULTISAMPLE);

			glEnable(GL_DEPTH_TEST);

			// Enable face culling
			glEnable(GL_CULL_FACE);
			glCullFace(GL_BACK);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int loadProgram(String v, String f) throws IOException {
		int program = 0;

		// Create Shaders
		// Vertex Shader
		File shaderv = new File(v);

		int vShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vShader,
				new String(Files.readAllBytes(shaderv.toPath())));
		glCompileShader(vShader);

		IntBuffer iVal = BufferUtils.createIntBuffer(1);
		glGetShaderiv(vShader, GL_COMPILE_STATUS, iVal);
		if (GL_TRUE != iVal.get() || true) {
			System.out.println("Vertex Shader Failed: " + v + "\n"
					+ glGetShaderInfoLog(vShader));
		}

		// Frag Shader
		File shaderf = new File(f);
		int fShader = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fShader,
				new String(Files.readAllBytes(shaderf.toPath())));
		glCompileShader(fShader);

		iVal = BufferUtils.createIntBuffer(1);
		glGetShaderiv(fShader, GL_COMPILE_STATUS, iVal);
		if (GL_TRUE != iVal.get() || true) {
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
		if (GL_TRUE != iVal.get() || true) {
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

		// Depth
		fboRenderBuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, fboRenderBuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, engine
				.getGame().getWidth(), engine.getGame().getHeight());
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT,
				GL_RENDERBUFFER, fboRenderBuffer);
		
		// Draw buffers
		IntBuffer fboBuffers = BufferUtils.createIntBuffer(3);
		int bfs[] = { GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1,
				GL_COLOR_ATTACHMENT2 };
		for (int i = 0; i < bfs.length; i++)
			fboBuffers.put(bfs[i]);
		fboBuffers.flip();

		glDrawBuffers(fboBuffers);


		// Errors
		if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
			System.out.println("FBO Failed: "
					+ glCheckFramebufferStatus(GL_FRAMEBUFFER));
		}
	}

	public long getWindowHandler() {
		return window;
	}

	public void setProgramMain() {
		glUseProgram(mainProgram);
	}
	
	public void setProgramDrawFBO() {
		glUseProgram(drawFBOProgram);
	}

	public void setRenderTargetFBO() {
		glBindFramebuffer(GL_FRAMEBUFFER, fbo);

		glViewport(0, 0, engine.getGame().getWidth(), engine.getGame()
				.getHeight());

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void setRenderTargetWindow() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		glViewport(0, 0, engine.getGame().getWidth(), engine.getGame()
				.getHeight());

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}

	public void drawFBO() {		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Bind the color
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, fboColorTexture);
		glUniform1i(uTextureColor, 0);

		// Bind the glow
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, fboGlowTexture);
		glUniform1i(uTextureGlow, 1);

		// Bind the id
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, fboIDTexture);
		glUniform1i(uTextureID, 2);
		
		// ? glEnableVertexAttribArray(0);

		glBegin(GL_QUADS);
		glTexCoord2f(0f, 0f);
		glVertex3f(-1.0f, -1.0f, 0.0f);
		glTexCoord2f(1f, 0f);
		glVertex3f(1.0f, -1.0f, 0.0f);
		glTexCoord2f(1f, 1f);
		glVertex3f(1.0f, 1.0f, 0.0f);
		glTexCoord2f(0f, 1f);
		glVertex3f(-1.0f, 1.0f, 0.0f);
		glEnd();

		// glDisableVertexAttribArray(0);
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
}
