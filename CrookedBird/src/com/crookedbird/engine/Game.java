package com.crookedbird.engine;

import java.awt.image.BufferedImage;

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

	final public BufferedImage draw() {
		view.getGraphics().clearRect(0, 0, getWidth(), getHeight());

		currentScene.draw(view);

		return view;
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
