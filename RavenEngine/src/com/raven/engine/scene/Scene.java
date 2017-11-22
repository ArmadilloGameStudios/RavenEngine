package com.raven.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.graphics3d.Camera;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.shader.WaterReflectionShader;
import com.raven.engine.graphics3d.shader.WaterRefractionShader;
import com.raven.engine.graphics3d.shader.WorldShader;
import com.raven.engine.graphics3d.shader.WorldWaterShader;
import com.raven.engine.worldobject.WorldObject;

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
        // 1 Draw world refraction water
        WaterRefractionShader waterRefrShader = window.getWaterRefractionShader();

        waterRefrShader.useProgram();
        waterRefrShader.setProjectionMatrix(camera.getProjectionMatrix());
        waterRefrShader.setViewMatrix(camera.getViewMatrix());

        for (Layer l : layers) {
            if (l.getDestination() != Layer.Destination.Water)
                for (WorldObject o : l.getGameObjectList()) {
                    waterRefrShader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }

        // 2 Draw world reflection water
        WaterReflectionShader waterReflShader = window.getWaterReflectionShader();

        waterReflShader.useProgram();
        waterReflShader.setProjectionMatrix(camera.getProjectionMatrix());
        waterReflShader.setViewMatrix(camera.getViewMatrix());

        for (Layer l : layers) {
            if (l.getDestination() != Layer.Destination.Water)
                for (WorldObject o : l.getGameObjectList()) {
                    waterReflShader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }

        // 3 Draw world above water
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

        // 4 Draw water
        WorldWaterShader worldWaterShader = window.getWorldWaterShader();

        worldWaterShader.useProgram(camera);
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
