package com.raven.engine2d.ui;

import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.graphics2d.shader.Shader;
import com.raven.engine2d.scene.Scene;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public abstract class UIText<S extends Scene, P extends UIContainer<S>>
        extends UIObject<S, P> {

    private com.raven.engine2d.worldobject.TextObject text;

    public UIText(S scene) {
        super(scene);
    }

    public void setTexture(com.raven.engine2d.worldobject.TextObject text) {
        this.text = text;
    }

    public com.raven.engine2d.worldobject.TextObject getTexture() {
        return text;
    }

    @Override
    public void draw(GameWindow window, Shader shader) {
//        shader.setProperties(this);

        if (text != null)
            text.draw();

        window.drawQuad();

        for (UIObject o : this.getChildren()) {
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

    public void setFont(Font font) {
        throw new NotImplementedException();
    }

    public void updateTexture() {
        text.updateTexture();
    }
}
