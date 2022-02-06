package com.armadillogamestudios.engine2d.idmap;

import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.scene.Scene;

public abstract class IDMapChild<S extends Scene<?>> implements MouseClickHandler {

    private final IDMapGameObject<S, ?> mapGameObject;

    public IDMapChild(IDMapGameObject<S, ?> mapGameObject) {
        this.mapGameObject = mapGameObject;
    }

    public abstract int getID();

    protected IDMapGameObject<S, ?> getMapGameObject() {
        return mapGameObject;
    }
}
