package com.raven.engine.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine.Game;
import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.graphics3d.shader.*;
import com.raven.engine.scene.light.Light;
import com.raven.engine.util.Vector3f;
import com.raven.engine.worldobject.HUDContainer;
import com.raven.engine.worldobject.HUDObject;
import com.raven.engine.worldobject.WorldObject;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14.glBlendEquation;
import static org.lwjgl.opengl.GL30.*;

public abstract class Scene<G extends Game> {
    private Layer<WorldObject> layerTerrain = new Layer(Layer.Destination.Terrain);
    private Layer<WorldObject> layerWater = new Layer(Layer.Destination.Water);
    private Layer<WorldObject> layerDetails = new Layer(Layer.Destination.Details);
    private Layer<HUDContainer> layerHUD = new Layer(Layer.Destination.HUD);

    private Vector3f backgroundColor;

    private boolean renderWater = false;

    private G game;

    private Camera camera;

    private List<Light> lights = new ArrayList<>();

    public Scene(G game) {
        this.game = game;

        camera = new Camera();
    }

    public Camera getCamera() {
        return camera;
    }

    final public void draw4ms(GameWindow window) {
        // update the matrix block
        Shader.setProjectionViewMatrices(camera);

        // 3 Draw world above water
        WorldMSShader worldMSShader = window.getWorldMSShader();
        worldMSShader.useProgram();

        for (WorldObject o : layerTerrain.getChildren()) {
            Shader.setModelMatrix(o.getModelMatrix());
            o.draw4ms();
        }

        for (WorldObject o : layerDetails.getChildren()) {
            Shader.setModelMatrix(o.getModelMatrix());
            o.draw4ms();
        }

        // 4 Draw water
//        WorldWaterShader worldWaterShader = window.getWorldWaterShader();
//
//        worldWaterShader.useProgram();
//
//        for (WorldObject o : layerWater.getObjectList()) {
//            Shader.setModelMatrix(o.getModelMatrix());
//            o.draw4ms();
//        }

        // Blit World
        worldMSShader.blitComplexValue();

        // Draw Complex Sample Stencil
        window.getComplexSampleStencilShader().useProgram();
        window.drawQuad();

        // Clear the Set the blend type

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);
        glBlendEquation(GL_FUNC_ADD);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glDisable(GL_DEPTH_TEST);

        // Accum lights
        for (Light light : lights) {
            switch (light.getLightType()) {
                case Light.AMBIANT:
                    break;
                case Light.GLOBAL_DIRECTIONAL:
                    // update the light block
                    Shader.setLight(light);

                    // Combine Simple
                    window.getSimpleDirLightShader().useProgram();
                    window.drawQuad();

                    // Combine Complex
                    window.getComplexDirectionalLightShader().useProgram();
                    window.drawQuad();
                    break;
            }
        }

        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);

        //
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
        for (Light light : lights) {
            switch (light.getLightType()) {
                case Light.AMBIANT:
                    break;
                case Light.GLOBAL_DIRECTIONAL:
                    // Update the light/shadow block
                    Shader.setLight(light);

                    // Gen Shadow
                    ShadowShader shadowShader = light.getShadowShader();
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
                    break;
            }
        }

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

        onUpdate(deltaTime);

        layerTerrain.update(deltaTime);
        layerWater.update(deltaTime);
        layerDetails.update(deltaTime);
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

    abstract public void enterScene();

    abstract public void exitScene();

    abstract public void onUpdate(float deltaTime);

    public void addLight(Light light) {
        lights.add(light);
    }

    public void removeLight(Light light) {
        lights.remove(light);
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
}
