package com.armadillogamestudios.tactics.gameengine.scene.battle;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

public class Tile<S extends BattleScene<?>> extends WorldObject<S, WorldObject<S, ?>> {

    public Tile(S scene, GameData data) {
        super(scene, data);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .1f;
    }

    @Override
    public void draw(LayerShader shader) {
        super.draw(shader);
    }
}
