package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

public class UIImage<S extends Scene<?>> extends UIObject<S> {

    private final Vector2f position = new Vector2f();
    private final Vector2f offset = new Vector2f();
    private SpriteSheet texture;

    private int width, height;
    private SpriteAnimationState spriteAnimation;

    public UIImage(S scene, int width, int height, String src) {
        super(scene);

        this.width = width;
        this.height = height;

        texture = scene.getEngine().getSpriteSheet(src);
        texture.load(scene);
    }

    public UIImage(S scene, String src) {
        super(scene);

        texture = scene.getEngine().getSpriteSheet(src);
        texture.load(scene);

        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public final void setSprite(String src) {
        texture = getScene().getEngine().getSpriteSheet(src);
        texture.load(getScene());
    }

    @Override
    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
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
    public void draw(LayerShader shader) {
        shader.draw(texture, spriteAnimation, getWorldPosition(), null, offset, getID(), getWorldZ(), getFade(), getHighlight(), DrawStyle.UI);
    }

    @Override
    public void update(float deltaTime) {
        if (spriteAnimation != null) {
            spriteAnimation.update(deltaTime);
        }

        this.onUpdate(deltaTime);

        for (UIObject c : getChildren()) {
            c.update(deltaTime);
        }
    }

    protected SpriteSheet getTexture() {
        return texture;
    }

    public void setAnimationAction(String action) {
        if (action.equals("idle"))
            this.spriteAnimation.setActionIdle();
        else
            this.spriteAnimation.setAction(action);
    }

    public SpriteAnimationState getSpriteAnimation() {
        return this.spriteAnimation;
    }

    public void setSpriteAnimation(SpriteAnimationState spriteAnimation) {
        this.spriteAnimation = spriteAnimation;
    }

    @Override
    public void release() {
        super.release();
        texture.release();
    }

    public float getInternalTextureYOffset() {
        return this.offset.y;
    }

    public void setInternalTextureYOffset(float y) {
        this.offset.y = y;
    }

    public float getInternalTextureXOffset() {
        return this.offset.x;
    }

    public void setInternalTextureXOffset(float x) {
        this.offset.x = x;
    }
}
