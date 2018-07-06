package com.raven.engine2d.worldobject;

import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.ui.UITextWriter;
import com.raven.engine2d.ui.UITexture;

public abstract class WorldTextObject
        <
                S extends Scene,
                P extends Parentable<WorldObject>>
        extends WorldObject<S, P, WorldObject> {

    private String text;

    private UITexture image;
    private UITextWriter textWriter;

    private UIFont font = new UIFont();

    public WorldTextObject(S scene) {
        super(scene);
    }

    public void setText(String text) {
        this.text = text;

        if (image == null) {
            if (font.isHighlight()) {
                image = new UITexture(getScene().getEngine(), 160, 12 * 2);
            } else {
                image = new UITexture(getScene().getEngine(), 160, 12);
            }

            spriteSheet = image;
        }

        // TODO don't remake each time
        textWriter = new UITextWriter(getScene().getEngine(), image, font);
        textWriter.clear();

        textWriter.write(text);

        image.load();
    }

    public String getText() {
        return text;
    }

    public UIFont getFont() {
        return font;
    }
}
