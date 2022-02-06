package com.armadillogamestudios.engine2d.idmap;

import com.armadillogamestudios.engine2d.input.MouseClickHandler;

public abstract class IDMapChild implements MouseClickHandler {

    private final IDMapGameObject<?, ?> mapGameObject;

    public IDMapChild(IDMapGameObject<?, ?> mapGameObject) {
        this.mapGameObject = mapGameObject;
    }

    public abstract int getID();

    protected IDMapGameObject<?, ?> getMapGameObject() {
        return mapGameObject;
    }
}
