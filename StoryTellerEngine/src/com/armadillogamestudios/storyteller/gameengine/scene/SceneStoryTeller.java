package com.armadillogamestudios.storyteller.gameengine.scene;

import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;

public abstract class SceneStoryTeller<G extends StoryTeller<G>>
        extends Scene<G> {

    public SceneStoryTeller(G game) {
        super(game);
    }
}
