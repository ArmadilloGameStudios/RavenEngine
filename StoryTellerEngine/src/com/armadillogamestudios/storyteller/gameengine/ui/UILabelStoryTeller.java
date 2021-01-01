package com.armadillogamestudios.storyteller.gameengine.ui;

import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;

public class UILabelStoryTeller<S extends SceneStoryTeller<? extends StoryTeller>> extends UILabel<S> {
    public UILabelStoryTeller(S scene, String text, int width, int height) {
        super(scene, text, width, height);

        setHighlight(this.getScene().getGame().getTextHighlight());
    }
}
