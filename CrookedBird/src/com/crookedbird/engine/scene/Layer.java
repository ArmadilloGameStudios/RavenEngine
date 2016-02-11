package com.crookedbird.engine.scene;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.crookedbird.engine.graphics.ImageReference;
import com.crookedbird.engine.input.MouseClickInput;
import com.crookedbird.engine.input.MouseMovementInput;
import com.crookedbird.engine.worldobject.Parentable;
import com.crookedbird.engine.worldobject.WorldObject;

public class Layer implements Parentable {
	private Scene scene;
	private List<WorldObject> gameObjectList = new CopyOnWriteArrayList<WorldObject>();
	private BufferedImage img;
	private boolean redraw = true;

	public Layer(Scene scene) {
		this.scene = scene;
		img = ImageReference.safeImage(new BufferedImage(10, 10,
				BufferedImage.TYPE_INT_RGB));
	}
	
	public List<WorldObject> getGameObjectList() {
		return gameObjectList;
	}
	
	public void addChild(WorldObject obj) {
		gameObjectList.add(obj);
	}
	
	public void requiresRedraw() {
		redraw = true;
	}

	public BufferedImage getImage() {
		return img;
	}
	
	public void draw(BufferedImage img) {
		if (redraw) {
			for (WorldObject o : gameObjectList) {
				o.draw(img);
			}
		}
	}
	
	final public void mouseMove(MouseMovementInput m) {
		for (WorldObject o : gameObjectList) {
			o.checkMouseMovement(m);
		}
	}

	public void mouseClick(MouseClickInput e) {
		for (WorldObject o : gameObjectList) {
			o.checkMouseClick(e);
		}
	}

	public void update(double deltaTime) {
		for (WorldObject o : gameObjectList) {
			o.update(deltaTime);
		}
	}

	@Override
	public int getGlobalX() {
		return 0;
	}

	@Override
	public int getGlobalY() {
		return 0;
	}

	@Override
	public int getWidth() {
		return scene.getWidth();
	}

	@Override
	public int getHeight() {
		return scene.getHeight();
	}
}