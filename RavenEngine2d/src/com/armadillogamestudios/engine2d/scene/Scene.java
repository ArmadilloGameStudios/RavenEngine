package com.armadillogamestudios.engine2d.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.armadillogamestudios.engine2d.Game;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.graphics2d.shader.CompilationShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.graphics2d.shader.TextShader;
import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.engine2d.worldobject.GameObject;
import com.armadillogamestudios.engine2d.ui.UITextWriter;
import com.armadillogamestudios.engine2d.ui.UIToolTip;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import javax.sound.sampled.Clip;

public abstract class Scene<G extends Game<G>> {
    private final Layer layerTerrain = new Layer(Layer.Destination.Terrain);
    private final Layer layerDetails = new Layer(Layer.Destination.Details);
    private final Layer layerEffects = new Layer(Layer.Destination.Effects);
    private final Layer layerUI = new Layer(Layer.Destination.UI);
    private final Layer layerToolTip = new Layer(Layer.Destination.ToolTip);

    private final Vector3f backgroundColor = new Vector3f(0,0,0);
    private final Vector2f worldOffset = new Vector2f();

    private UIToolTip<?> toolTip;

    private final List<GameObject<?>> children = new ArrayList<>();
    private final List<UITextWriter> toWrite = new ArrayList<>();
    private final List<ShaderTexture> textures = new CopyOnWriteArrayList<>();
    private final List<KeyboardHandler> keyboardHandlers = new ArrayList<>();

    private boolean paused = false;
    private float time = 0;

    private final G game;

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
        LayerShader layerShader = window.getLayerShader();
        layerShader.useProgram();

        // drawImage
        drawLayer(layerTerrain, layerShader);
        drawLayer(layerDetails, layerShader);
        drawLayer(layerEffects, layerShader);

        // ui
        drawLayer(layerUI, layerShader);
        drawLayer(layerToolTip, layerShader);

        // compile
        CompilationShader compilationShader = window.getCompilationShader();
        compilationShader.useProgram();

        compilationShader.clear(backgroundColor);
        window.printErrors("Draw Clear Error: ");
        compilationShader.compile(layerTerrain.getRenderTarget());
        compilationShader.compile(layerDetails.getRenderTarget());
        compilationShader.compile(layerEffects.getRenderTarget());

        compilationShader.clearDepthBuffer();
        compilationShader.compile(layerUI.getRenderTarget());
        compilationShader.drawColorOnly();
        compilationShader.compile(layerToolTip.getRenderTarget());

        window.printErrors("Draw Compile Error: ");
        compilationShader.blitToScreen();
        window.printErrors("Draw Blit Error: ");
    }

    private void drawLayer(Layer layer, LayerShader layerShader) {
        if (layer.isNeedRedraw()) {
            layerShader.clear(layer.getRenderTarget(), backgroundColor);
            for (GameObject o : layer.getChildren()) {
                if (o.isVisible())
                    o.draw(layerShader);

                getEngine().getWindow().printErrors("Draw " + layer.getDestination() + " Error: ");
            }

            layer.setNeedRedraw(false);
        }
    }

    final public void update(float deltaTime) {
        time += deltaTime;

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

    public List<GameObject<?>> getChildren() {
        return children;
    }

    public void addChild(GameObject<?> child) {
        if (!children.contains(child))
            children.add(child);
    }

    public void addGameObject(GameObject<?> go) {
        getLayer(go.getDestination()).addChild(go);

        go.getChildren().forEach(this::addGameObject);
    }

    public void removeGameObject(GameObject<?> go) {
        Layer layer = getLayer(go.getDestination());

        if (layer != null) {
            layer.removeChild(go);
        }

        go.getChildren().forEach(this::removeGameObject);
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

        for (GameObject<?> obj : layerTerrain.getChildren()) {
            obj.release();
        }

        for (GameObject<?> obj : layerDetails.getChildren()) {
            obj.release();
        }

        for (GameObject<?> obj : layerEffects.getChildren()) {
            obj.release();
        }

        for (GameObject<?> obj : layerUI.getChildren()) {
            obj.release();
        }

        for (GameObject<?> obj : layerToolTip.getChildren()) {
            obj.release();
        }

        for (ShaderTexture sheet : getShaderTextures()) {
            sheet.release();
        }
    }

    abstract public void onExitScene();

    abstract public void onUpdate(float deltaTime);

    public void setBackgroundColor(Vector3f color) {
        backgroundColor.set(color);
    }

    public G getGame() {
        return game;
    }

    public float getTime() {
        return time;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
    }

    private final KeyData key = new KeyData();
    public void inputKey(int key, int action, int mods) {

        if (action == GLFW.GLFW_PRESS) {
            this.key.update(key, mods);

            keyboardHandlers.forEach(keyboardHandler -> keyboardHandler.onKeyPress(this.key));
        } else if (action == GLFW.GLFW_RELEASE) {
            this.key.update(key, mods);

            keyboardHandlers.forEach(keyboardHandler -> keyboardHandler.onKeyRelease(this.key));
        }
    }

    public void addKeyboardHandler(KeyboardHandler keyboardHandler) {
        keyboardHandlers.add(keyboardHandler);
    }

    public Clip getClip(String name) {
        return null;
    }

    protected void setToolTip(UIToolTip<?> toolTip) {
        if (this.toolTip != null)
            removeGameObject(this.toolTip);

        this.toolTip = toolTip;

        this.layerToolTip.addChild(toolTip);
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

    public abstract DrawStyle getDrawStyle();
}
