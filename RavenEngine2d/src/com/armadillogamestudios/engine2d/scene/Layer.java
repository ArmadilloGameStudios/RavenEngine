package com.armadillogamestudios.engine2d.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.armadillogamestudios.engine2d.graphics2d.shader.IDMapShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.IDMapRenderTarget;
import com.armadillogamestudios.engine2d.graphics2d.shader.rendertarget.RenderTarget;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.engine2d.worldobject.GameObject;

public class Layer {

    public enum Destination {
        Terrain, UI, Details, Effects, ToolTip
    }

    private boolean needRedraw = true;
    private final Vector3f backgroundColor = new Vector3f(0,0,0);

    private final Destination destination;
    private final List<GameObject<?>> gameObjectList = new CopyOnWriteArrayList<>();

    private final RenderTarget renderTarget;

    public Layer(Destination destination) {
        this.destination = destination;

        renderTarget = new RenderTarget(LayerShader.COLOR, LayerShader.ID, LayerShader.DEPTH, false);
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

    public void draw(LayerShader layerShader) {
        if (isNeedRedraw()) {
            layerShader.clear(renderTarget, backgroundColor);
            for (GameObject<?> o : getChildren()) {
                if (o.isVisible())
                    o.draw(layerShader);
            }

            setNeedRedraw(false);
        }
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