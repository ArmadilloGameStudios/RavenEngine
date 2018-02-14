package com.raven.engine.worldobject;

import com.raven.engine.graphics3d.GameWindow;
import com.raven.engine.graphics3d.shader.HUDShader;
import com.raven.engine.scene.Layer;
import com.raven.engine.scene.Scene;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class HUDText<S extends Scene, P extends HUDContainer<S>>
        extends HUDObject<S, P> {

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

    @Override
    public void draw(GameWindow window, HUDShader shader) {
        shader.setProperties(this);
        if (text != null)
            text.draw();

        window.drawQuad();

        for (HUDObject o : this.getChildren()) {
            if (o.getVisibility())
                o.draw(window, shader);
        }
    }

    @Override
    public void release() {
        super.release();

        text.release();
    }

    public void setText(String text) {
        throw new NotImplementedException();
    }
}
