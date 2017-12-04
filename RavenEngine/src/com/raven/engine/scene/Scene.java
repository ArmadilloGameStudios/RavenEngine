package com.raven.engine.scene;

import java.util.List;

import com.raven.engine.GameProperties;
import com.raven.engine.graphics3d.GameWindow3D;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.shader.*;
import com.raven.engine.scene.light.DirectionalLight;
import com.raven.engine.worldobject.WorldObject;

public abstract class Scene {
    private Layer layerTerrain = new Layer(Layer.Destination.Terrain);
    private Layer layerWater = new Layer(Layer.Destination.Water);
    private Layer layerDetails = new Layer(Layer.Destination.Details);

    private Camera camera;
    private DirectionalLight light = new DirectionalLight("sunLight");

    public Scene() {
        camera = new Camera();
    }

    public Camera getCamera() {
        return camera;
    }

    public DirectionalLight getDirectionalLight() {
        return light;
    }

    final public void draw(GameWindow3D window) {
        // update the sun block
        Shader.setSunLight(light);
        Shader.setProjectionViewMatrices(camera);

        // 1 Draw world refraction water
        WaterRefractionShader waterRefrShader = window.getWaterRefractionShader();

        waterRefrShader.useProgram();

        for (WorldObject o : layerTerrain.getGameObjectList()) {
            Shader.setModelMatrix(o.getModelMatirx());
            o.draw();
        }

        // 2 Draw world reflection water
        WaterReflectionShader waterReflShader = window.getWaterReflectionShader();

        if (GameProperties.getReflectTerrain()) {
            waterReflShader.useProgram();

            for (WorldObject o : layerTerrain.getGameObjectList()) {
                Shader.setModelMatrix(o.getModelMatirx());
                o.draw();
            }

            if (GameProperties.getReflectObjects())
                for (WorldObject o : layerDetails.getGameObjectList()) {
                    Shader.setModelMatrix(o.getModelMatirx());
                    o.draw();
                }
        }

        // 3 Draw world above water
        WorldShader worldShader = window.getWorldShader();

        worldShader.useProgram();

        for (WorldObject o : layerTerrain.getGameObjectList()) {
            Shader.setModelMatrix(o.getModelMatirx());
            o.draw();
        }

        for (WorldObject o : layerDetails.getGameObjectList()) {
            Shader.setModelMatrix(o.getModelMatirx());
            o.draw();
        }

        // 4 Draw water
        WorldWaterShader worldWaterShader = window.getWorldWaterShader();

        worldWaterShader.useProgram();

        for (WorldObject o : layerWater.getGameObjectList()) {
            Shader.setModelMatrix(o.getModelMatirx());
            o.draw();
        }
    }

    final public void update(float deltaTime) {
        camera.update(deltaTime);

        onUpdate(deltaTime);

        layerTerrain.update(deltaTime);
        layerWater.update(deltaTime);
        layerDetails.update(deltaTime);
    }

    final public Layer getLayerTerrain() {
        return layerTerrain;
    }

    final public Layer getLayerWater() {
        return layerWater;
    }

    final public Layer getLayerDetails() {
        return layerDetails;
    }

    public Layer getLayer(Layer.Destination destination) {
        switch (destination) {
            case Water:
                return getLayerWater();
            case Terrain:
                return getLayerTerrain();
            case Details:
            default:
                return getLayerDetails();
        }
    }

    abstract public List<ModelData> getSceneModels();

    abstract public void enterScene();

    abstract public void exitScene();

    abstract public void onUpdate(float deltaTime);

}
