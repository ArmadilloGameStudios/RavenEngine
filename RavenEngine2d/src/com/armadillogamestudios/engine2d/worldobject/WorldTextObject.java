package com.armadillogamestudios.engine2d.worldobject;

import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UITextWriter;
import com.armadillogamestudios.engine2d.ui.UITexture;

public abstract class WorldTextObject
        <S extends Scene<?>>
        extends WorldObject<S, WorldObject<S, ?>> {

    private String text;

    private UITexture image;
    private final UITextWriter textWriter;

    private final UIFont font = new UIFont();

    public WorldTextObject(S scene) {
        super(scene);

        textWriter = new UITextWriter(getScene().getEngine(), getScene());
    }

    public void setText(String text) {
        if (this.text == null || !this.text.equals(text)) {
            this.text = text;

            if (image == null) {
                if (font.isHighlight() || font.isWrap()) {
                    image = new UITexture(getScene().getEngine(), 160, 12 * 2);
                } else {
                    image = new UITexture(getScene().getEngine(), 160, 12);
                }

                getScene().getEngine().getWindow().printErrors("pre cat (wt) ");
                image.load(getScene());
                getScene().getEngine().getWindow().printErrors("post cat (wt) ");
                spriteSheet = image;
            }

            textWriter.setImageDest(image);
            textWriter.setText(text, font);

            getScene().addTextToWrite(textWriter);
        }
    }

    public String getText() {
        return text;
    }

    public UIFont getFont() {
        return font;
    }

    @Override
    public void setScene(S scene) {
        if (image != null)
            image.load(scene);
        this.text = null;
        super.setScene(scene);
    }

    @Override
    public void release() {
        super.release();
        if (image != null)
            image.release();
    }
}
