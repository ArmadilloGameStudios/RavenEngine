package com.raven.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.Game;
import com.raven.engine.graphics3d.Camera;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.shader.WaterShader;
import com.raven.engine.graphics3d.shader.WorldShader;
import com.raven.engine.graphics3d.shader.WorldWaterShader;
import com.raven.engine.worldobject.WorldObject;

import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL30.GL_CLIP_DISTANCE0;

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

    final public void draw(GameWindow3D window) {
        // 1 Draw world under water

        WaterShader waterShader = window.getWaterShader();

        waterShader.useProgram();
        waterShader.setProjectionMatrix(camera.getProjectionMatrix());
        waterShader.setViewMatrix(camera.getViewMatrix());

        for (Layer l : layers) {
            if (l.getDestination() != Layer.Destination.Water)
                for (WorldObject o : l.getGameObjectList()) {
                    waterShader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }

        // 2 Draw world above water
        WorldShader worldShader = window.getWorldShader();

        worldShader.useProgram();
        worldShader.setProjectionMatrix(camera.getProjectionMatrix());
        worldShader.setViewMatrix(camera.getViewMatrix());

        for (Layer l : layers) {
            if (l.getDestination() != Layer.Destination.Water)
                for (WorldObject o : l.getGameObjectList()) {
                    worldShader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }

        // 3 Draw water
        WorldWaterShader worldWaterShader = window.getWorldWaterShader();

        worldWaterShader.useProgram();
        worldWaterShader.setProjectionMatrix(camera.getProjectionMatrix());
        worldWaterShader.setViewMatrix(camera.getViewMatrix());

        for (Layer l : layers) {
            if (l.getDestination() == Layer.Destination.Water)
                for (WorldObject o : l.getGameObjectList()) {
                    worldWaterShader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }
    }

    final public void update(float deltaTime) {
        camera.update(deltaTime);

        for (Layer l : layers) {
            l.update(deltaTime);
        }
    }

    abstract public List<ModelData> getSceneModels();

    abstract public void enterScene();

    abstract public void exitScene();

}
