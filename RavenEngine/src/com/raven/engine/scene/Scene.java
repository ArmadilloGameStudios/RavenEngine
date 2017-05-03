package com.raven.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.Game;
import com.raven.engine.input.MouseClickInput;
import com.raven.engine.input.MouseMovementInput;

public abstract class Scene {
	private Game game;
	private List<Layer> layers = new ArrayList<Layer>();
	
	public Scene(Game game) {
		this.game = game;
	}
	
	protected int getWidth() {
		return game.getWidth();
	}

	protected int getHeight() {
		return game.getHeight();
	}
	
	protected List<Layer> getLayers() {
		return layers;
	}
	
	protected void addLayer(Layer l) {
		layers.add(l);
	}
	
	final public void draw() {
		for (Layer l : layers) {
			l.draw();
		}
	}

	final public void mouseMove(MouseMovementInput e) {
		for (Layer l : layers) {			
			l.mouseMove(e);
		}
	}

	final public void mouseClick(MouseClickInput e) {
		for (Layer l : layers) {			
			l.mouseClick(e);
		}
	}

	final public void update(float deltaTime) {
		for (Layer l : layers) {
			l.update(deltaTime);
		}
	}

	abstract public void enterScene();
	abstract public void exitScene();
}
