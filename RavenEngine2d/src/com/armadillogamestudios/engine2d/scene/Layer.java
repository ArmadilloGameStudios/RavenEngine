package com.armadillogamestudios.engine2d.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.RenderTarget;
import com.armadillogamestudios.engine2d.worldobject.GameObject;

public class Layer {

    public enum Destination {
        Terrain, UI, Details, Effects, ToolTip
    }

    private boolean needRedraw = true;

    private final Destination destination;
    private final List<GameObject<?>> gameObjectList = new CopyOnWriteArrayList<>();

    private final RenderTarget renderTarget;

    public Layer(Destination destination) {
        this.destination = destination;
        renderTarget = new RenderTarget(LayerShader.COLOR, LayerShader.ID, LayerShader.DEPTH, true);
    }

    public List<GameObject<?>> getChildren() {
        return gameObjectList;
    }

    public void addChild(GameObject<?> obj) {
        if (!gameObjectList.contains(obj))
            gameObjectList.add(obj);
    }

    public void setNeedRedraw(boolean needRedraw) {
        this.needRedraw = needRedraw;
    }

    public boolean isNeedRedraw() {
        return true; // needRedraw;
    }

    public void removeChild(GameObject<?> obj) {
        gameObjectList.remove(obj);
    }

    public Destination getDestination() {
        return destination;
    }

    public RenderTarget getRenderTarget() {
        return renderTarget;
    }
}