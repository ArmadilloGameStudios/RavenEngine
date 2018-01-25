package com.raven.engine.worldobject;

import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;

public abstract class HUDContainer<S extends Scene>
        extends HUDObject<S, Layer<HUDObject>> {

    public HUDContainer(S scene) {
        super(scene);
    }
}
