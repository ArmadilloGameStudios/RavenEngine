package com.armadillogamestudios.engine2d.ui;

import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public abstract class UITextButton<S extends Scene>
    extends  UIButton<S> {

    private UIText<S> uiText;

    private int w = 8, h = 10;

    public UITextButton(S scene, String text, String btnImgSrc, String animation) {
        super(scene, btnImgSrc, animation);

        uiText = new UIText<S>(scene, text) {
            @Override
            public SpriteAnimationState getSpriteAnimationState() {
                return UITextButton.this.getSpriteAnimationState();
            }

            @Override
            public float getHeight() {
                return UITextButton.this.getHeight();
            }

            @Override
            public float getWidth() {
                return UITextButton.this.getWidth();
            }
        };

        uiText.getFont().setX(w);
        uiText.getFont().setY(h);
        uiText.getFont().setButton(true);
        addChild(uiText);
    }

    public void setTextHighlight(Highlight textHighlight) {
        uiText.setHighlight(textHighlight);
    }

    public void load() {
        uiText.load();
    }

    public void setText(String text) {
        uiText.setText(text);
    }

    public UIFont getFont() {
        return uiText.getFont();
    }

    public String getText() {
        return uiText.getText();
    }

    protected void setColorFeed(UITextColorFeed colorFeed) {
        uiText.setColorFeed(colorFeed);
    }
}
