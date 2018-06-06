package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.Childable;
import com.raven.engine2d.worldobject.Parentable;

public final class UIImage<S extends Scene> extends UIObject<S, Parentable<UIObject>> {

    private Vector2f position = new Vector2f();
    private UITexture texture;

    private int width, height;
    private SpriteAnimationState spriteAnimation;

    public UIImage(S scene, int width, int height, String src) {
        super(scene);

        this.width = width;
        this.height = height;

        texture = new UITexture(src);
        texture.load();
    }

    @Override
    public int getStyle() {
        return 0;
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
    public float getY() {
        return position.y;
    }

    @Override
    public void setY(float y) {
        position.y = y;
    }

    @Override
    public float getX() {
        return position.x;
    }

    @Override
    public void setX(float x) {
        position.x = x;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public void draw(MainShader shader) {
        shader.draw(texture, spriteAnimation, getWorldPosition(), getScene().getWorldOffset(), getID(), getZ(), null, DrawStyle.UI);
    }

    public void setSpriteAnimation(SpriteAnimationState spriteAnimation) {
        this.spriteAnimation = spriteAnimation;
    }
}
