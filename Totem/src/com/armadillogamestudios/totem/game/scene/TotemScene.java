package com.armadillogamestudios.totem.game.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.totem.game.TotemGame;

public abstract class TotemScene
        extends Scene<TotemGame> {

    public TotemScene(TotemGame game) {
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
