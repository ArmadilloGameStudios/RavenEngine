package com.armadillogamestudios.tactics.gameengine.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;

public abstract class TacticsScene<G extends TacticsGame<G>>
        extends Scene<G> {

    public TacticsScene(G game) {
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
    public final void onUpdate(float update) {
        updateUI(update);
    }

    @Override
    public final DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD_1;
    }

    public abstract void updateUI(float deltaTime);

    protected abstract void loadUI();
}
