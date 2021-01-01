package com.armadillogamestudios.storyteller.gameengine.ui;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;

public abstract class UITextButtonStoryTeller<S extends SceneStoryTeller<? extends StoryTeller>> extends UITextButton<S> {

    public UITextButtonStoryTeller(S scene, String text, String btnImgSrc, String animation) {
        super(scene, text, btnImgSrc, animation);

        setTextHighlight(this.getScene().getGame().getTextHighlight());
    }
}
