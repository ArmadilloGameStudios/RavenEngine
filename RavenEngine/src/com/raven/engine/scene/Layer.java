package com.raven.engine.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

public class Layer implements Parentable {
	private Scene scene;
	private List<WorldObject> gameObjectList = new CopyOnWriteArrayList<WorldObject>();

	public Layer(Scene scene) {
		this.scene = scene;
	}

	public List<WorldObject> getGameObjectList() {
		return gameObjectList;
	}

	public void addChild(WorldObject obj) {
		gameObjectList.add(obj);
	}

	public void draw() {
		for (WorldObject o : gameObjectList) {
			o.draw();
		}
	}

	public void update(float deltaTime) {
		for (WorldObject o : gameObjectList) {
			o.update(deltaTime);
		}
	}

	@Override
	public double getGlobalX() {
		return 0;
	}

	@Override
	public double getGlobalY() {
		return 0;
	}
	
	@Override
	public double getGlobalZ() {
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
	
	@Override
	public int getLength() {
		return 0;
	}
}