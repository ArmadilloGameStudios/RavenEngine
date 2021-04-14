package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Scene;

public abstract class Tile implements MouseHandler {

    public abstract String getSprite();

    public ShaderTexture getSpriteSheet(Scene<?> scene) {
        return scene.getEngine().getSpriteSheet(getSprite());
    }

    public void load(Scene<?> scene) {  // TODO load all textures somewhere else
        SpriteSheet texture = scene.getEngine().getSpriteSheet(getSprite());
        texture.load(scene);
    }
}
