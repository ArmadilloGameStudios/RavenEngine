package com.raven.engine2d.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.graphics2d.shader.TextShader;
import com.raven.engine2d.ui.UITextWriter;
import com.raven.engine2d.ui.UIToolTip;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import com.raven.engine2d.worldobject.GameObject;
import com.raven.engine2d.worldobject.Parentable;

import javax.sound.sampled.Clip;

import static org.lwjgl.opengl.GL11.glFinish;

public abstract class Scene<G extends Game<G>> implements Parentable<GameObject> {
    private Layer layerTerrain = new Layer(Layer.Destination.Terrain);
    private Layer layerDetails = new Layer(Layer.Destination.Details);
    private Layer layerEffects = new Layer(Layer.Destination.Effects);
    private Layer layerUI = new Layer(Layer.Destination.UI);
    private Layer layerToolTip = new Layer(Layer.Destination.ToolTip);

    private Vector3f backgroundColor = new Vector3f();
    private Vector2f worldOffset = new Vector2f();

    private UIToolTip toolTip;

    private List<GameObject> children = new ArrayList<>();
    private List<UITextWriter> toWrite = new ArrayList<>();
    private List<ShaderTexture> textures = new CopyOnWriteArrayList<>();

    private boolean paused = false;

    private G game;

    public Scene(G game) {
        this.game = game;
    }

    public GameEngine<G> getEngine() {
        return game.getEngine();
    }

    final public void draw(GameWindow window) {

        // text
        if (toWrite.size() > 0) {
            TextShader textShader = window.getTextShader();
            textShader.useProgram();

            toWrite.forEach(textWriter -> {
                textWriter.write(textShader);
                window.printErrors("Draw Text Error: ");
            });

            toWrite.clear();
        }

        // shader
        MainShader mainShader = window.getMainShader();
        mainShader.useProgram();

        mainShader.clear(backgroundColor);

        // drawImage
        for (GameObject o : layerTerrain.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw Terrain Error: ");
        }

        for (GameObject o : layerDetails.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw Details Error: ");
        }

        for (GameObject o : layerEffects.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw Effects Error: ");
        }

        // ui
        mainShader.clearDepthBuffer();

        for (GameObject o : layerUI.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw UI Error: ");
        }

        for (GameObject o : layerToolTip.getChildren()) {
            if (o.isVisible())
                o.draw(mainShader);
            window.printErrors("Draw ToolTip Error: ");
        }


        mainShader.blitToScreen();
    }

    final public void update(float deltaTime) {
        if (!isPaused())
            onUpdate(deltaTime);

        if (!isPaused()) {
            getChildren().forEach(c -> c.update(deltaTime));
        }
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
            case ToolTip:
                return layerToolTip;
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

        go.getChildren().forEach(c -> {
            if (c instanceof GameObject)
                addGameObject((GameObject) c);
        });
    }

    public void removeGameObject(GameObject go) {
        Layer layer = getLayer(go.getDestination());

        if (layer != null) {
            layer.removeChild(go);
        }

        go.getChildren().forEach(c -> {
            if (c instanceof GameObject)
                removeGameObject((GameObject) c);
        });
    }

    public void addTextToWrite(UITextWriter textWriter) {
        toWrite.add(textWriter);
    }

    abstract public void loadShaderTextures();

    public final List<ShaderTexture> getShaderTextures() {
        return textures;
    }

    public final void addLoadedShaderTexture(ShaderTexture texture) {
        if (!textures.contains(texture))
            textures.add(texture);
    }

    public Vector2f getWorldOffset() {
        return worldOffset;
    }

    public final void enterScene() {
        loadShaderTextures();
        getEngine().getWindow().printErrors("pre load (scene) ");
        for (ShaderTexture sheet : getShaderTextures()) {
            sheet.load(this);
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

        for (ShaderTexture sheet : getShaderTextures()) {
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

    public Clip getClip(String name) {
        return null;
    }

    protected void setToolTip(UIToolTip toolTip) {
        if (this.toolTip != null)
            removeGameObject(this.toolTip);

        this.toolTip = toolTip;

        this.layerUI.addChild(toolTip);
        addGameObject(toolTip);
    }

    public void showToolTip(String title, String text) {
        if (toolTip != null) {

            toolTip.setTitle(title);
            toolTip.setText(text);

            toolTip.setVisibility(true);
        }
    }

    public void hideToolTip() {
        if (toolTip != null) {
            toolTip.setVisibility(false);
        }
    }

    public UIToolTip getToolTip() {
        return toolTip;
    }
}
