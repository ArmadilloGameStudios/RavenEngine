package com.armadillogamestudios.storyteller.gameengine.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class ExitButton
        extends UITextButton<MainMenuScene<?>> {

    public ExitButton(MainMenuScene<?> scene) {
        super(scene, "exit", "sprites/button.png", "mainbutton");

        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().getGame().exit();
    }
}
