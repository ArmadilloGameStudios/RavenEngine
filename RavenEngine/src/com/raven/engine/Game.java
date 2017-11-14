package com.raven.engine;

import java.awt.image.BufferedImage;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.ModelReference;
import com.raven.engine.scene.Scene;

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

	final public void draw3d() {
		currentScene.draw();
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

	abstract public void setup();

	abstract public void breakdown();

	abstract public Scene loadInitialScene();

	abstract public int getWidth();

	abstract public int getHeight();

	abstract public String getTitle();

	abstract public String getMainDirectory();
}
