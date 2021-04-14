package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class UITextInput<S extends Scene<?>> extends UIObject<S>
    implements KeyboardHandler {

    private final Vector2f position = new Vector2f();
    private UITextColorFeed colorFeed;

    private final UILabel<S> text;
    private final UIImage<S> background;

    private String displayText, displayTextAppend;
    private boolean displayAppend = true;
    private final float period = 500;

    private boolean unset = true;

    private final int max = 23;

    public UITextInput(S scene, String defaultText, String backgroundSrc) {
        super(scene);

        background = new UIImage<>(scene, backgroundSrc);
        text = new UILabel<>(scene, defaultText, 151, 15);
        // text.setZ(.9f);
        UIFont font = text.getFont();
        font.setSmall(true);

        text.setX(12);
        text.setY(-5);

        addChild(background);
        addChild(text);

        text.load();

        scene.addKeyboardHandler(this);
    }

    public String getText() {
        return displayText;
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
    public float getHeight() {
        return background.getHeight();
    }

    @Override
    public float getWidth() {
        return background.getWidth();
    }

    @Override
    public void onKeyPress(KeyData d) {
        if (unset) {
            displayText = "";
            unset = false;
        }

        if (d.getKey() == KeyData.Key.BACKSPACE) {
            if (displayText.length() > 0)
                displayText = displayText.substring(0, displayText.length() - 1);
        } else {
            if (displayText.length() < max)
                displayText = displayText + d.getKey().toString();
        }

        displayTextAppend = displayText + "|";

        this.text.setText(displayTextAppend);
        displayAppend = true;

        time = 0f;

        this.text.load();
    }

    @Override
    public void onKeyRelease(KeyData i) {

    }

    private float time;
    @Override
    public void onUpdate(float deltaTime) {
        super.onUpdate(deltaTime);

        if (!unset) {
            time += deltaTime;

            if (time > period) {
                time -= period;

                if (displayAppend) {
                    this.text.setText(displayText);
                } else {
                    this.text.setText(displayTextAppend);
                }

                this.text.load();

                displayAppend = !displayAppend;
            }
        }
    }

    public void setTextHighlight(Highlight highlight) {
        this.text.setHighlight(highlight);
    }
}
