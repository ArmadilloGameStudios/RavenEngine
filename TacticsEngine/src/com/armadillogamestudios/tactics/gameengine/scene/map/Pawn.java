package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

public abstract class Pawn {

    public abstract String getSprite();

    public final ShaderTexture getSpriteSheet(Scene<?> scene) {
        return scene.getEngine().getSpriteSheet(getSprite());
    }

    public final void load(Scene<?> scene) {  // TODO load all textures somewhere else
        SpriteSheet texture = scene.getEngine().getSpriteSheet(getSprite());
        texture.load(scene);
    }

}
