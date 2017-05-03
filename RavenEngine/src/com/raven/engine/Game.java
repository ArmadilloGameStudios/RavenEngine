package com.raven.engine;

import static org.lwjgl.opengl.GL11.glFlush;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import java.awt.image.BufferedImage;

import com.raven.engine.input.MouseMovementInput;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Matrix4f;

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
		// Enable the custom mode attribute
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);

		// Set Projection
		engine.getWindow().setProjectionMatrix(
				Matrix4f.perspective(60.0f, ((float) getWidth())
						/ ((float) getHeight()), 1f, 1000.0f));

		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix = viewMatrix.multiply(Matrix4f.translate(0f, 0f, -30f));
		viewMatrix = viewMatrix.multiply(Matrix4f.rotate(65f, 1f, 0f, 0f));
		viewMatrix = viewMatrix.multiply(Matrix4f.rotate(trans * 100.0f, 0f, 1f, 0f));

		engine.getWindow().setViewMatrix(viewMatrix);

		engine.getWindow().setModelMatrix(new Matrix4f());

		trans -= .002;

		currentScene.draw();

		glFlush();

		// Disable the custom mode attribute
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
	}

	final public void mouseMove(MouseMovementInput e) {
		currentScene.mouseMove(e);
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
