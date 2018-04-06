package com.raven.engine2d.scene;

import java.util.List;

import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.TerrainShader;
import com.raven.engine.graphics3d.model.ModelData;
import com.raven.engine2d.scene.light.GlobalDirectionalLight;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.worldobject.WorldObject;

public abstract class Scene<G extends com.raven.engine2d.Game> {
    private com.raven.engine2d.scene.Layer<WorldObject> layerTerrain = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Terrain);
    private com.raven.engine2d.scene.Layer<WorldObject> layerWater = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Water);
    private com.raven.engine2d.scene.Layer<WorldObject> layerDetails = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Details);
    private com.raven.engine2d.scene.Layer<UIContainer> layerUI = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.UI);

    private Vector3f backgroundColor = new Vector3f();

    private boolean renderWater = false, paused = false;

    private G game;

    private Camera camera;

    private GlobalDirectionalLight globalDirectionalLight;

    public Scene(G game) {
        this.game = game;

        camera = new Camera();
    }

    public Camera getCamera() {
        return camera;
    }

    final public void draw(GameWindow window) {
        // shader
        TerrainShader terrainShader = window.getTerrainShader();
        terrainShader.useProgram();

        // draw
        for (com.raven.engine2d.worldobject.WorldObject o : layerTerrain.getChildren()) {
            o.draw();
            window.printErrors("Draw Error: ");
        }

        for (com.raven.engine2d.worldobject.WorldObject o : layerDetails.getChildren()) {
            o.draw();
        }

        for (com.raven.engine2d.worldobject.WorldObject o : layerWater.getChildren()) {
            o.draw();
        }
    }

    final public void update(float deltaTime) {
        camera.update(deltaTime);

        if (!isPaused()) {
            onUpdate(deltaTime);

            layerTerrain.update(deltaTime);
            layerWater.update(deltaTime);
            layerDetails.update(deltaTime);
        }

        layerUI.update(deltaTime);
    }

    final public com.raven.engine2d.scene.Layer<WorldObject> getLayerTerrain() {
        return layerTerrain;
    }

    final public com.raven.engine2d.scene.Layer<WorldObject> getLayerWater() {
        return layerWater;
    }

    final public com.raven.engine2d.scene.Layer<WorldObject> getLayerDetails() {
        return layerDetails;
    }

    final public com.raven.engine2d.scene.Layer<UIContainer> getLayerUI() {
        return layerUI;
    }

    public com.raven.engine2d.scene.Layer getLayer(com.raven.engine2d.scene.Layer.Destination destination) {
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

    public final void enterScene() {
        onEnterScene();
    }

    abstract public void onEnterScene();

    public final void exitScene() {
        onExitScene();

        for (com.raven.engine2d.worldobject.GameObject obj : layerTerrain.getChildren()) {
            obj.release();
        }

        for (com.raven.engine2d.worldobject.GameObject obj : layerWater.getChildren()) {
            obj.release();
        }

        for (com.raven.engine2d.worldobject.GameObject obj : layerDetails.getChildren()) {
            obj.release();
        }

        for (com.raven.engine2d.worldobject.GameObject obj : layerUI.getChildren()) {
            obj.release();
        }

        for (ModelData modelData : getSceneModels()) {
            modelData.release();
        }

        globalDirectionalLight.release();
    }

    abstract public void onExitScene();

    abstract public void onUpdate(float deltaTime);

    public void setGlobalDirectionalLight(GlobalDirectionalLight light) {
        globalDirectionalLight = light;
    }

    public void removeGlobalDirectionalLight() {
        if (globalDirectionalLight != null)
            globalDirectionalLight.release();
    }

    public void setRenderWater(boolean renderWater) {
        this.renderWater = renderWater;
    }

    public boolean getRenderWater() {
        return renderWater;
    }

    public void setBackgroundColor(Vector3f color) {
        backgroundColor = color;
    }

    public G getGame() {
        return game;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;

        camera.setInteractable(!paused);
    }

    public boolean isPaused() {
        return paused;
    }

    public abstract void inputKey(int key, int action, int mods);
}
