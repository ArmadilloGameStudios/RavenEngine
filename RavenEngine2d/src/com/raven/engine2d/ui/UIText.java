package com.raven.engine2d.ui;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public abstract class UIText<S extends Scene, P extends UIContainer<S>>
        extends UIObject<S, P> {

    private SpriteSheet spriteSheet;
    private SpriteAnimationState spriteAnimationState;

    private Vector2f position = new Vector2f();

    public UIText(S scene, GameData data) {
        super(scene);

        if (data.has("sprite")) {
            spriteSheet = GameEngine.getEngine().getSpriteSheet(data.getString("sprite"));

            if (data.has("animation")) {
                String animationName = data.getString("animation");
                spriteAnimationState = new SpriteAnimationState(GameEngine.getEngine().getAnimation(animationName));
            }
        }
    }

    public void draw(MainShader shader) {
        shader.draw(spriteSheet, spriteAnimationState, position, getScene().getWorldOffset(), getID(), getZ(), null, DrawStyle.UI);

        for (UIObject o : this.getChildren()) {
            if (o.getVisibility())
                o.draw(shader);
        }
    }

    public void setAnimationAction(String action) {
        this.spriteAnimationState.setAction(action);
    }

    public SpriteAnimationState getSpriteAnimationState() {
        return spriteAnimationState;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (spriteAnimationState != null) {
            spriteAnimationState.update(deltaTime);
        }

        for (UIObject o : this.getChildren()) {
            o.update(deltaTime);
        }
    }

    @Override
    public void release() {
        super.release();
    }
}
