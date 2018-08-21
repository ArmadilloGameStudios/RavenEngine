package com.raven.engine2d.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.worldobject.GameObject;

public class Layer {

    public enum Destination {
        Terrain, UI, Details, Effects, TOOLTIP
    }

    private Destination destination;
    private List<GameObject> gameObjectList = new CopyOnWriteArrayList<>();

    public Layer(Destination destination) {
        this.destination = destination;
    }

    public List<GameObject> getChildren() {
        return gameObjectList;
    }

    public void addChild(GameObject obj) {
        if (!gameObjectList.contains(obj))
            gameObjectList.add(obj);
    }

    public void removeChild(GameObject obj) {
        gameObjectList.remove(obj);
    }

    public Destination getDestination() {
        return destination;
    }
}