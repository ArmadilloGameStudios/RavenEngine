package com.crookedbird.engine.scene;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.crookedbird.engine.Game;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;

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
	
	final public void draw(BufferedImage img) {
		for (Layer l : layers) {
			l.draw(img);
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

	final public void update(double deltaTime) {
		for (Layer l : layers) {
			l.update(deltaTime);
		}
	}

	abstract public void enterScene();
	abstract public void exitScene();
}
