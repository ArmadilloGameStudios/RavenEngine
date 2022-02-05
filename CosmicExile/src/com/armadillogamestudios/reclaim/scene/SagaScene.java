package com.armadillogamestudios.reclaim.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.reclaim.ReclaimGame;

public abstract class SagaScene
        extends Scene<ReclaimGame> {

    public SagaScene(ReclaimGame game) {
        super(game);
    }

    @Override
    public final void loadShaderTextures() {

    }

    @Override
    public final void onEnterScene() {
        loadUI();
    }

    @Override
    public final void onExitScene() {

    }

    @Override
    public final DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD_1;
    }

    protected abstract void loadUI();
}
