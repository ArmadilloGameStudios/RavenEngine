package com.armadillogamestudios.mouseepic.scenes.worldscene.entity;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.worldobject.WorldObject;

public abstract class Entity extends WorldObject<WorldScene, WorldScene, WorldObject> {
    public Entity(WorldScene scene, GameData data) {
        super(scene, data);
    }
}
