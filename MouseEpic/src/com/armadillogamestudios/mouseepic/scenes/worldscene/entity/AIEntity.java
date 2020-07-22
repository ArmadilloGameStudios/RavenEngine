package com.armadillogamestudios.mouseepic.scenes.worldscene.entity;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector4f;

public class AIEntity extends Entity {

    private Vector4f size;
    private Vector2f center;

    public AIEntity(WorldScene scene, GameData data) {
        super(scene, data);

        size = new Vector4f(.2f, .05f, .6f, .5f);
        center = new Vector2f(size.x + size.z / 2f, size.y + size.w / 2f);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Details;
    }

    @Override
    public Vector4f getRect() {
        return size;
    }

    @Override
    public float getZ() {
        return .2f;
    }
}
