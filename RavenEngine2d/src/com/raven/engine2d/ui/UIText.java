package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.graphics2d.shader.MainShader;
import com.raven.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.worldobject.Parentable;

public abstract class UIText<S extends Scene>
        extends UIObject<S, UIObject<S, Parentable<UIObject>>> {

    private String text;
    private final String backgroundSrc;
    private UITexture image;
    private UITextWriter textWriter;

    private UIFont font = new UIFont();

    private Vector2f position = new Vector2f();

    public UIText(S scene, String text) {
        this(scene, text, null);
    }

    public UIText(S scene, String text, String backgroundSrc) {
        super(scene);

        this.text = text;
        this.backgroundSrc = backgroundSrc;
    }

    public UIFont getFont() {
        return font;
    }

    public void load() {
        if (image == null)
            if (font.isButton())
                image = new UITexture(getScene().getEngine(), (int) getWidth(), (int) getHeight() * 2);
            else
                image = new UITexture(getScene().getEngine(), (int) getWidth(), (int) getHeight());

        // TODO don't remake each time
        textWriter = new UITextWriter(getScene().getEngine(), image, font);

        if (backgroundSrc != null)
            textWriter.drawBackground(backgroundSrc);
        else
            textWriter.clear();

        textWriter.write(text);

        image.load();
    }

    public void draw(MainShader shader) {
        shader.draw(image, getSpriteAnimationState(), getWorldPosition(), getScene().getWorldOffset(), getID(), getZ(), true, null, DrawStyle.UI);
    }

    public void setAnimationAction(String action) {
        this.getSpriteAnimationState().setAction(action);
    }

    public abstract SpriteAnimationState getSpriteAnimationState();

    @Override
    public float getHeight() {
        return 64;
    }

    @Override
    public float getWidth() {
        return 100;
    }

    @Override
    public final float getY() {
        return position.y;
    }

    @Override
    public final void setY(float y) {
        position.y = y;
    }

    @Override
    public final float getX() {
        return position.x;
    }

    @Override
    public final void setX(float x) {
        position.x = x;
    }

    @Override
    public final Vector2f getPosition() {
        return position;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (getSpriteAnimationState() != null) {
            getSpriteAnimationState().update(deltaTime);
        }

        for (UIObject o : this.getChildren()) {
            o.update(deltaTime);
        }
    }

    @Override
    public void release() {
        super.release();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
