package com.armadillogamestudios.storyteller.gameengine.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;

public abstract class SceneStoryTeller<G extends StoryTeller<G>>
        extends Scene<G> {

    public SceneStoryTeller(G game) {
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
        return DrawStyle.STANDARD;
    }

    public abstract void updateUI(float deltaTime);

    protected abstract void loadUI();
}
