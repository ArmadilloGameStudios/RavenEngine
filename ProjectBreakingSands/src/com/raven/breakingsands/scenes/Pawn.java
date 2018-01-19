package com.raven.breakingsands.scenes;

import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Scene;
import com.raven.engine.worldobject.WorldObject;

public abstract class Pawn extends WorldObject {

    public Pawn(Scene scene, int team, ModelData model) {
        super(scene, model);
    }
}
