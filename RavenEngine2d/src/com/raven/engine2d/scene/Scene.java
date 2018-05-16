package com.raven.engine2d.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.raven.engine2d.Game;
import com.raven.engine2d.audio.AudioSource;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.ScreenQuad;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.ui.UIObject;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.WorldObject;

import javax.sound.sampled.Clip;

public abstract class Scene<G extends Game> {
    private Layer<WorldObject> layerTerrain = new Layer<>(Layer.Destination.Terrain);
    private Layer<WorldObject> layerDetails = new Layer<>(Layer.Destination.Details);
    private Layer<UIContainer> layerUI = new Layer<>(Layer.Destination.UI);

    private Map<String, AudioSource> audioSources = new HashMap<>();

    private Vector3f backgroundColor = new Vector3f();
    private Vector2f worldOffset = new Vector2f();

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


        // draw
        for (WorldObject o : layerTerrain.getChildren()) {
            if (o.getVisibility())
                o.draw(mainShader);
            window.printErrors("Draw World Object Error: ");
        }

        for (WorldObject o : layerDetails.getChildren()) {
            if (o.getVisibility())
                o.draw(mainShader);
        }

        // ui
        for (UIObject o : layerUI.getChildren()) {
            if (o.getVisibility())
                o.draw(mainShader);
            window.printErrors("Draw UI Error: ");
        }

        mainShader.blitToScreen();
    }

    final public void update(float deltaTime) {

        if (!isPaused()) {
            onUpdate(deltaTime);

            layerTerrain.update(deltaTime);
            layerDetails.update(deltaTime);
        }

        layerUI.update(deltaTime);
    }

    final public Layer<WorldObject> getLayerTerrain() {
        return layerTerrain;
    }

    final public Layer<WorldObject> getLayerDetails() {
        return layerDetails;
    }

    final public Layer<UIContainer> getLayerUI() {
        return layerUI;
    }

    public Layer getLayer(Layer.Destination destination) {
        switch (destination) {
            case Terrain:
                return getLayerTerrain();
            case Details:
            default:
                return getLayerDetails();
        }
    }

    abstract public List<SpriteSheet> getSpriteSheets();

//    abstract public List<String> getAudioSourcesNames();

    public Vector2f getWorldOffset() {
        return worldOffset;
    }

    public final void enterScene() {
        for (SpriteSheet sheet : getSpriteSheets()) {
            sheet.load();
        }

//        for (String audioName : getAudioSourcesNames()) {
//            audioSources.put(audioName, new AudioSource(audioName));
//        }

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
