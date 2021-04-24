package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.graphics2d.shader.LayerShader;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.engine2d.util.math.Vector2f;

import java.util.Objects;

public abstract class UIText<S extends Scene<?>>
        extends UIObject<S> {

    private final String backgroundSrc;
    private String text;
    private boolean needLoad = true;
    private final String currentText;
    private UITexture image;
    private final UITextWriter textWriter;

    private final UIFont font = new UIFont();

    private final Vector2f position = new Vector2f();
    private UITextColorFeed colorFeed;

    public UIText(S scene, String text) {
        this(scene, text, null);
    }

    public UIText(S scene, String text, String backgroundSrc) {
        super(scene);

        currentText = this.text = text;
        this.backgroundSrc = backgroundSrc;

        textWriter = new UITextWriter(getScene().getEngine(), getScene());
    }

    public UIFont getFont() {
        return font;
    }

    public void load() {
        if (needLoad)
            this.load(null);

        needLoad = false;
    }

    public void load(UITextWriterHandler handler) {
        if (image == null) {
            if (font.isButton())
                image = new UITexture(getScene().getEngine(), (int) getWidth(), (int) getHeight() * 2);
            else
                image = new UITexture(getScene().getEngine(), (int) getWidth(), (int) getHeight());

            image.load(getScene());
        }

        if (backgroundSrc != null)
            textWriter.setBackground(backgroundSrc);

        textWriter.setImageDest(image);
        textWriter.setText(text, font);
        textWriter.setColorFeed(colorFeed);
        textWriter.setHandler(handler);

        needsRedraw();

        getScene().addTextToWrite(textWriter);
    }

    public void draw(LayerShader shader) {
        try {
            shader.draw(image, getSpriteAnimationState(), getWorldPosition(), null, null, getID(), getWorldZ(), getFade(), getHighlight(), DrawStyle.UI);
        } catch (Exception e) {
            System.out.println(this.getText());
            throw e;
        }
    }

    @Override
    public float getZ() {
        return super.getZ() + .02f;
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

        for (UIObject<?> o : this.getChildren()) {
            o.update(deltaTime);
        }
    }

    @Override
    public void release() {
        super.release();
        if (image != null)
            image.release();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (!Objects.equals(this.text, text)) {
            this.text = text;
            needLoad = true;
        }
    }

    public void setColorFeed(UITextColorFeed colorFeed) {
        this.colorFeed = colorFeed;
    }
}
