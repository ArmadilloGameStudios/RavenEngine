package com.raven.engine.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine.GameEngine;
import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.Parentable;
import com.raven.engine.worldobject.WorldObject;

public class Layer<C extends Childable>
        implements Parentable<C> {

    public enum Destination {Terrain, Water, HUD, Details}

    private Scene scene;
    private Destination destination;
    private List<C> gameObjectList = new CopyOnWriteArrayList<>();

    private GameWindow window;

    public Layer(Destination destination) {
        this.destination = destination;
        this.scene = scene;

        this.window = GameEngine.getEngine().getWindow();
    }

    @Override
    public List<C> getChildren() {
        return gameObjectList;
    }

    public void addChild(C obj) {
        obj.setParent(this);
        gameObjectList.add(obj);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void update(float deltaTime) {
        for (C o : gameObjectList) {
            o.update(deltaTime);
        }
    }

    public Destination getDestination() {
        return destination;
    }
}