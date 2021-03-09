package com.armadillogamestudios.storyteller.gameengine.scene;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;

public abstract class CreatePlayerScene<G extends StoryTeller<G>> extends SceneStoryTeller<G> implements KeyboardHandler {

    public CreatePlayerScene(G game) {
        super(game);
    }

    @Override
    public void onInputKey(KeyData i) {

    }
}