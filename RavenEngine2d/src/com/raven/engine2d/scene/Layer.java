package com.raven.engine2d.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.GameWindow;

public class Layer<C extends com.raven.engine2d.worldobject.Childable>
        implements com.raven.engine2d.worldobject.Parentable<C> {

    public enum Destination { Terrain, UI, Details}

    private Scene scene;
    private Destination destination;
    private List<C> gameObjectList = new CopyOnWriteArrayList<>();

    private GameWindow window;

    public Layer(Destination destination) {
        this.destination = destination;

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