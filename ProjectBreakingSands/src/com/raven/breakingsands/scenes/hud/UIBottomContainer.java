package com.raven.breakingsands.scenes.hud;

import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector4f;
import com.raven.engine2d.ui.UIContainer;

public class UIBottomContainer<S extends Scene>
        extends UIContainer<S> {

    public UIBottomContainer(S scene) {
        super(scene);
    }

    @Override
    public int getStyle() {
        return UIContainer.BOTTOM;
    }
}
