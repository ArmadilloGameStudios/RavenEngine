package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.graphics2d.shader.ShaderTexture;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.input.MouseHandler;
import com.armadillogamestudios.engine2d.scene.Scene;

import java.util.List;

public abstract class Tile<U extends Pawn> {

    public abstract String getSprite();

    public final ShaderTexture getSpriteSheet(Scene<?> scene) {
        return scene.getEngine().getSpriteSheet(getSprite());
    }

    public final void load(Scene<?> scene) {  // TODO load all textures somewhere else
        SpriteSheet texture = scene.getEngine().getSpriteSheet(getSprite());
        texture.load(scene);
    }

    public abstract int getX();
    public abstract int getY();

    public abstract List<U> getPawns();

    public abstract boolean isInCognito();
}
