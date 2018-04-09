package com.raven.engine2d.scene;

import java.util.List;

import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.ScreenQuad;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.worldobject.WorldObject;

public abstract class Scene<G extends com.raven.engine2d.Game> {
    private com.raven.engine2d.scene.Layer<WorldObject> layerTerrain = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Terrain);
    private com.raven.engine2d.scene.Layer<WorldObject> layerWater = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Water);
    private com.raven.engine2d.scene.Layer<WorldObject> layerDetails = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.Details);
    private com.raven.engine2d.scene.Layer<UIContainer> layerUI = new com.raven.engine2d.scene.Layer<>(com.raven.engine2d.scene.Layer.Destination.UI);

    private Vector3f backgroundColor = new Vector3f();

    private boolean paused = false;

    private G game;

    public Scene(G game) {
        this.game = game;
    }

    final public void draw(GameWindow window) {
        // shader
        MainShader mainShader = window.getMainShader();
        mainShader.useProgram();

        // draw
        for (WorldObject o : layerTerrain.getChildren()) {
            o.draw(mainShader);
            window.printErrors("Draw Error: ");
        }

        for (WorldObject o : layerDetails.getChildren()) {
            o.draw(mainShader);
        }

        for (WorldObject o : layerWater.getChildren()) {
            o.draw(mainShader);
        }
    }

    final public void update(float deltaTime) {

        if (!isPaused()) {
            onUpdate(deltaTime);

            layerTerrain.update(deltaTime);
            layerWater.update(deltaTime);
            layerDetails.update(deltaTime);
        }

        layerUI.update(deltaTime);
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

    final public Layer<UIContainer> getLayerUI() {
        return layerUI;
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

    abstract public List<SpriteSheet> getSpriteSheets();

    public final void enterScene() {
        for (SpriteSheet sheet : getSpriteSheets()) {
            sheet.load();
        }

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

        for (SpriteSheet sheet : getSpriteSheets()) {
            sheet.release();
        }
    }

    abstract public void onExitScene();

    abstract public void onUpdate(float deltaTime);

    public void setBackgroundColor(Vector3f color) {
        backgroundColor = color;
    }

    public G getGame() {
        return game;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    public abstract void inputKey(int key, int action, int mods);
}
