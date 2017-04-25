package com.crookedbird.engine;

import java.awt.image.BufferedImage;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.opengl.GL;

import com.crookedbird.engine.graphics3d.GLMatrix;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;
import com.crookedbird.engine.scene.Scene;

public abstract class Game {
	private GameEngine engine;
	private BufferedImage view;
	private Scene currentScene;
	private Scene readyTransitionScene;
	private boolean isrunning = false;

	public Game() {
		isrunning = true;
	}

	public GameEngine getEngine() {
		return engine;
	}

	public void setEngine(GameEngine engine) {
		this.engine = engine;
		this.view = new BufferedImage(getWidth(), getHeight(),
				BufferedImage.TYPE_INT_RGB);
	}

	protected BufferedImage getImage() {
		return view;
	}

	final public Scene getCurrentScene() {
		return currentScene;
	}

	float trans = 0;

	final public void draw3d(long window) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the
															// framebuffer

		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);


		// Set Projection
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glMultMatrixf(GLMatrix.perspective(60.0f, ((float) getWidth())
				/ ((float) getHeight()), 1f, 1000.0f));

		glTranslatef(0f, 0f, -100.0f);
		glRotatef(45.0f, 1f, 0f, 0f);
		glRotatef(trans * 200.0f, 0f, 1f, 0f);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		trans -= .003;

		currentScene.draw();

		glFlush();

		glfwSwapBuffers(window); // swap the color buffers

		// Poll for window events. The key callback above will only be
		// invoked during this call.
		glfwPollEvents();
	}

	final public void mouseMove(MouseMovementInput e) {
		currentScene.mouseMove(e);
	}

	final public void mouseClick(MouseClickInput e) {
		currentScene.mouseClick(e);
	}

	final public void update(float deltaTime) {
		currentScene.update(deltaTime);

		if (readyTransitionScene != null) {
			transitionScene(readyTransitionScene);
		}
	}

	final protected void transitionScene(Scene scene) {
		if (currentScene != null) {
			currentScene.exitScene();
		}
		scene.enterScene();
		currentScene = scene;
		readyTransitionScene = null;
	}

	final public void prepTransitionScene(Scene scene) {
		readyTransitionScene = scene;
	}

	final public boolean isRunning() {
		return isrunning;
	}

	abstract public void setup();

	abstract public void breakdown();

	abstract public void loadInitialScene();

	abstract public int getWidth();

	abstract public int getHeight();

	abstract public String getTitle();
}
