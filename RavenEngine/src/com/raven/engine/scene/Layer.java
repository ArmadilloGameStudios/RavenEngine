package com.raven.engine.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

public class Layer implements Parentable {
	public enum Destination { Normal, Water };

	private Scene scene;
	private Destination destination;
	private List<WorldObject> gameObjectList = new CopyOnWriteArrayList<WorldObject>();

	private GameWindow3D window;

	public Layer(Scene scene) {
		this(scene, Destination.Normal);
	}

	public Layer(Scene scene, Destination destination) {
		this.destination = destination;
		this.scene = scene;

		this.window = GameEngine.getEngine().getWindow();
	}

	public List<WorldObject> getGameObjectList() {
		return gameObjectList;
	}

	public void addChild(WorldObject obj) {
		gameObjectList.add(obj);
	}

	float trans = 0;
	public void draw() {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix = viewMatrix.multiply(Matrix4f.translate(0f, 0f, -30f));
        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(40f, 1f, 0f, 0f));
        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(trans * 100f, 0f, 1f, 0f));

        trans += .0001 * GameEngine.getEngine().getDeltaTime();

		switch (destination) {
			case Water:
				window.getWaterShader().useProgram();

                window.getWaterShader().setProjectionMatrix(
                        Matrix4f.perspective(60.0f, ((float) getWidth())
                                / ((float) getHeight()), 10f, 100.0f));
                window.getWaterShader().setViewMatrix(viewMatrix);
                window.getWaterShader().setModelMatrix(new Matrix4f());

                for (WorldObject o : gameObjectList) {
                    o.draw();
                }
				break;
			case Normal:
			default:
				window.getWorldShader().useProgram();

                window.getWorldShader().setProjectionMatrix(
                        Matrix4f.perspective(60.0f, ((float) getWidth())
                                / ((float) getHeight()), 10f, 100.0f));
                window.getWorldShader().setViewMatrix(viewMatrix);
                window.getWorldShader().setModelMatrix(new Matrix4f());

                for (WorldObject o : gameObjectList) {
                    o.draw();
                }
				break;
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