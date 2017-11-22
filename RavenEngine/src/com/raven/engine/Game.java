package com.raven.engine;

import java.awt.image.BufferedImage;

import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.ModelReference;
import com.raven.engine.scene.Scene;

public abstract class Game {
	private GameEngine engine;
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
	}

	final public Scene getCurrentScene() {
		return currentScene;
	}

	final public void renderWorld(GameWindow3D window) {
		currentScene.draw(window);
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

		ModelReference.clearBuffers();
		ModelReference.loadBlankModel();
		for (ModelData md : scene.getSceneModels()) {
			ModelReference.load(md);
		}
		ModelReference.compileBuffer();

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

	final protected void setRunning(boolean running) {
		this.isrunning = running;
	}

	abstract public void setup();

	abstract public void breakdown();

	abstract public Scene loadInitialScene();

	abstract public String getTitle();

	abstract public String getMainDirectory();
}
