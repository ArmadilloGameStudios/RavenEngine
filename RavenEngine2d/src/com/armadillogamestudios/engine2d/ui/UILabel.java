package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.scene.Scene;

public class UILabel<S extends Scene<?>>
        extends UIText<S> {

    private final int width, height;

    public UILabel(S scene, String text, int width, int height) {
        super(scene, text);

        this.width = width;
        this.height = height;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public SpriteAnimationState getSpriteAnimationState() {
        return null;
    }
}
