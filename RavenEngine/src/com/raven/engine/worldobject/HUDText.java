package com.raven.engine.worldobject;

import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.shader.HUDShader;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;

public abstract class HUDText<S extends Scene, C extends HUDContainer<S>> extends HUDObject<S, C> {

    private TextObject text;

    public HUDText(S scene) {
        super(scene);
    }

    public void setTexture(TextObject text) {
        this.text = text;
    }

    public TextObject getTexture() {
        return text;
    }

    public void draw(GameWindow window, HUDShader shader) {
        shader.setProperties(this);
        if (text != null)
            text.draw();

        window.drawQuad();

        for (HUDObject o : this.getChildren()) {
            o.draw(window, shader);
        }
    }
}
