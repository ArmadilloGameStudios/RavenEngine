package com.crookedbird.engine;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_INDEX_ARRAY;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.*;

import java.awt.image.BufferedImage;

import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;
import com.crookedbird.engine.scene.Scene;
import com.crookedbird.engine.util.Matrix4f;

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

	final public void draw3d() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Enable the custom mode attribute
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		// Set Projection
		// glMatrixMode(GL_PROJECTION);
		// glLoadIdentity();

		engine.getWindow().setProjectionMatrix(
				Matrix4f.perspective(60.0f, ((float) getWidth())
						/ ((float) getHeight()), 1f, 1000.0f));

		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix = viewMatrix.multiply(Matrix4f.translate(0f, 0f, -30f));
		viewMatrix = viewMatrix.multiply(Matrix4f.rotate(65f, 1f, 0f, 0f));

		engine.getWindow().setViewMatrix(viewMatrix);

		engine.getWindow().setModelMatrix(new Matrix4f());

		// glRotatef(trans * 100.0f, 0f, 1f, 0f);

		trans -= .002;

		currentScene.draw();

		glFlush();

		// Enable the custom mode attribute
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
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
