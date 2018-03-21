package com.raven.engine.scene;

import java.util.List;

import com.raven.engine.Game;
import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.model.ModelData;
import com.raven.engine.graphics3d.shader.*;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.GameObject;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.WorldObject;

public abstract class Scene<G extends Game> {
    private Layer<WorldObject> layerTerrain = new Layer<>(Layer.Destination.Terrain);
    private Layer<WorldObject> layerWater = new Layer<>(Layer.Destination.Water);
    private Layer<WorldObject> layerDetails = new Layer<>(Layer.Destination.Details);
    private Layer<HUDContainer> layerHUD = new Layer<>(Layer.Destination.HUD);

    private Vector3f backgroundColor;

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

    final public void draw4fw(GameWindow window) {

        // Update the light/shadow block
        Shader.setLight(globalDirectionalLight);

        // Gen Shadow
        ShadowShader shadowShader = globalDirectionalLight.getShadowShader();
        shadowShader.useProgram();

        for (WorldObject o : layerDetails.getChildren()) {
            if (o.getVisibility())
                o.drawShadow();
        }

        for (WorldObject o : layerTerrain.getChildren()) {
            if (o.getVisibility())
                o.drawShadow();
        }

        // update the matrix block
        Shader.setProjectionViewMatrices(camera);


        // 3 Draw world above water
        WorldMSShader worldMSShader = window.getWorldMSShader();
        worldMSShader.useProgram(backgroundColor);

        for (WorldObject o : layerTerrain.getChildren()) {
            worldMSShader.setHighlight(o.getHighlight());
            Shader.setModelMatrix(o.getModelMatrix());
            o.draw4ms(worldMSShader);
        }

        for (WorldObject o : layerDetails.getChildren()) {
            worldMSShader.setHighlight(o.getHighlight());
            Shader.setModelMatrix(o.getModelMatrix());
            o.draw4ms(worldMSShader);
        }

        // HUD
        HUDMSShader hudShader = window.getHUDMSShader();
        hudShader.useProgram();

        for (HUDContainer o : layerHUD.getChildren()) {
            if (o.getVisibility())
                o.draw(window, hudShader);

        }

        // Blit World
        worldMSShader.blitComplexValue();
    }

    final public void draw4(GameWindow window) {
        // update the matrix block
        Shader.setProjectionViewMatrices(camera);

        // Draw the world
        WorldShader worldShader = window.getWorldShader();
        worldShader.useProgram(backgroundColor);

        for (WorldObject o : layerDetails.getChildren()) {
            if (o.getVisibility()) {
                worldShader.setHighlight(o.getHighlight());
                o.draw4();
            }
        }

        for (WorldObject o : layerTerrain.getChildren()) {
            if (o.getVisibility()) {
                worldShader.setHighlight(o.getHighlight());
                o.draw4();
            }
        }

        // Lights
        LightShader.clear();

        // Accum lights

        // Update the light/shadow block
        Shader.setLight(globalDirectionalLight);

        // Gen Shadow
        ShadowShader shadowShader = globalDirectionalLight.getShadowShader();
        shadowShader.useProgram();

        for (WorldObject o : layerDetails.getChildren()) {
            if (o.getVisibility())
                o.draw4();
        }

        for (WorldObject o : layerTerrain.getChildren()) {
            if (o.getVisibility())
                o.draw4();
        }

        // Draw the light
        window.getDirLightShader().useProgram();
        window.drawQuad();


        // Water
        if (renderWater) {
            WaterShader waterShader = window.getWaterShader();
            waterShader.useProgram();

            for (WorldObject o : layerWater.getChildren()) {
                if (o.getVisibility())
                    o.draw4();
            }

            // Combine
            window.getCombinationShader().useProgram();
            window.drawQuad();

            // Highlight
        } else {
            // Highlight
            window.getHighlightShader().useProgram();
            window.drawQuad();
        }

        // HUD
        HUDShader hudShader = window.getHUDShader();
        hudShader.useProgram();
        for (HUDContainer o : layerHUD.getChildren()) {
            if (o.getVisibility())
                o.draw(window, hudShader);
        }

        // FXAA
        window.getFXAAShader().useProgram(renderWater);
        window.drawQuad();
    }

    final public void draw2(GameWindow window) {
        // Basic Shader
        BasicShader basicShader = window.getBasicShader();
        basicShader.useProgram();

        window.printErrors("Use Shader Error: ");

        // set uniforms
        basicShader.setUnifromProjectionMatrix(camera.getProjectionMatrix());
        basicShader.setUnifromVertexMatrix(camera.getViewMatrix());
        window.printErrors("Set Uniform Error: ");

        // basicShader.setUniformLight(lights.get(0));

        // draw

        basicShader.useProgram();

        for (WorldObject o : layerTerrain.getChildren()) {
            basicShader.setUnifromModelMatrix(o.getModelMatrix());
            o.draw2();
            window.printErrors("Draw Error: ");
        }

        for (WorldObject o : layerDetails.getChildren()) {
            basicShader.setUnifromModelMatrix(o.getModelMatrix());
            o.draw2();
        }

        for (WorldObject o : layerWater.getChildren()) {
            basicShader.setUnifromModelMatrix(o.getModelMatrix());
            o.draw2();
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

        layerHUD.update(deltaTime);
    }

    final public Layer<WorldObject> getLayerTerrain() {
        return layerTerrain;
    }

    final public Layer<WorldObject> getLayerWater() {
        return layerWater;
    }

    final public Layer<WorldObject> getLayerDetails() {
        return layerDetails;
    }

    final public Layer<HUDContainer> getLayerHUD() {
        return layerHUD;
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

    public final void enterScene() {
        onEnterScene();
    }

    abstract public void onEnterScene();

    public final void exitScene() {
        onExitScene();

        for (GameObject obj : layerTerrain.getChildren()) {
            obj.release();
        }

        for (GameObject obj : layerWater.getChildren()) {
            obj.release();
        }

        for (GameObject obj : layerDetails.getChildren()) {
            obj.release();
        }

        for (GameObject obj : layerHUD.getChildren()) {
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
