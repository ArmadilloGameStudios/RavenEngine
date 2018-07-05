package com.raven.engine2d.scene;

import java.util.ArrayList;
import java.util.List;

import com.raven.engine2d.Game;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.ui.UIObject;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.Parentable;
import com.raven.engine2d.worldobject.WorldObject;

import javax.sound.sampled.Clip;

public abstract class Scene<G extends Game> implements Parentable<GameObject> {
    private Layer layerTerrain = new Layer(Layer.Destination.Terrain);
    private Layer layerDetails = new Layer(Layer.Destination.Details);
    private Layer layerEffects = new Layer(Layer.Destination.Effects);
    private Layer layerUI = new Layer(Layer.Destination.UI);

    private Vector3f backgroundColor = new Vector3f();
    private Vector2f worldOffset = new Vector2f();

    private List<GameObject> children = new ArrayList<>();

    private boolean paused = false;

    private G game;

    public Scene(G game) {
        this.game = game;
    }

    final public void draw(GameWindow window) {
        // shader

        MainShader mainShader = window.getMainShader();
        mainShader.useProgram();

        mainShader.clear(backgroundColor);


        // drawImage
        for (GameObject o : layerTerrain.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw World Object Error: ");
        }

        for (GameObject o : layerDetails.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
        }

        for (GameObject o : layerEffects.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
        }

        // ui
        for (GameObject o : layerUI.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw UI Error: ");
        }

        mainShader.blitToScreen();
    }

    final public void update(float deltaTime) {
        onUpdate(deltaTime);

        if (!isPaused()) {
            getChildren().forEach(c -> c.update(deltaTime));
        }
    }

    private Layer getLayerTerrain() {
        return layerTerrain;
    }

    private Layer getLayerDetails() {
        return layerDetails;
    }

    private Layer getLayerUI() {
        return layerUI;
    }

    public Layer getLayer(Layer.Destination destination) {
        switch (destination) {
            case Terrain:
                return layerTerrain;
            case Details:
                return layerDetails;
            case Effects:
                return layerEffects;
            case UI:
                return layerUI;
        }

        return null;
    }

    @Override
    public List<GameObject> getChildren() {
        return children;
    }

    @Override
    public void addChild(GameObject child) {
        if (!children.contains(child))
            children.add(child);
    }

    public void addGameObject(GameObject go) {
        getLayer(go.getDestination()).addChild(go);
    }

    public void removeGameObject(GameObject go) {
        getLayer(go.getDestination()).removeChild(go);
    }

    abstract public List<SpriteSheet> getSpriteSheets();

    public Vector2f getWorldOffset() {
        return worldOffset;
    }

    public final void enterScene() {
        for (SpriteSheet sheet : getSpriteSheets()) {
            sheet.load();
        }

        onEnterScene();
    }

    abstract public void onEnterScene();

    public final void exitScene() {
        onExitScene();

        for (GameObject obj : layerTerrain.getChildren()) {
            obj.release();
        }

        for (GameObject obj : layerDetails.getChildren()) {
            obj.release();
        }

        for (GameObject obj : layerUI.getChildren()) {
            obj.release();
        }

        for (SpriteSheet sheet : getSpriteSheets()) {
            sheet.release();
        }

        Shader.clearTextureID();
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

    public Clip getClip(String name) {
        return null;
    }

}
