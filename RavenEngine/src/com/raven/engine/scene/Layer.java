package com.raven.engine.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.Camera;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

public class Layer implements Parentable {

    public enum Destination { Normal, Water };

	private Scene scene;
	private Destination destination;
	private List<WorldObject> gameObjectList = new CopyOnWriteArrayList<>();

	private GameWindow3D window;

	public Layer() {
		this(Destination.Normal);
	}

	public Layer(Destination destination) {
		this.destination = destination;
		this.scene = scene;

		this.window = GameEngine.getEngine().getWindow();
	}

	public List<WorldObject> getGameObjectList() {
		return gameObjectList;
	}

	@Override
	public void addChild(WorldObject obj) {
		obj.setParent(this);
		gameObjectList.add(obj);
	}

    public void setScene(Scene scene) {
        this.scene = scene;
    }


	public void update(float deltaTime) {
		for (WorldObject o : gameObjectList) {
			o.update(deltaTime);
		}
	}

	@Override
	public float getGlobalX() {
		return 0;
	}

	@Override
	public float getGlobalY() {
		return 0;
	}
	
	@Override
	public float getGlobalZ() {
		return 0;
	}

	public Destination getDestination() {
	    return destination;
    }
}