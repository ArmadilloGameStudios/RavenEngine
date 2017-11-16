package com.raven.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.Game;
import com.raven.engine.graphics3d.Camera;
import com.raven.engine.graphics3d.ModelData;

public abstract class Scene {
	private List<Layer> layers = new ArrayList<>();
	private Camera camera;
	
	public Scene() {
        camera = new Camera();
	}

    public Camera getCamera() {
        return camera;
    }

	protected List<Layer> getLayers() {
		return layers;
	}
	
	protected void addLayer(Layer l) {
		l.setScene(this);
		layers.add(l);
	}
	
	final public void draw() {
		for (Layer l : layers) {
			l.draw(camera);
		}
	}

	final public void update(float deltaTime) {
		for (Layer l : layers) {
			l.update(deltaTime);
		}
	}

    abstract public List<ModelData> getSceneModels();
    abstract public void enterScene();
	abstract public void exitScene();

}
