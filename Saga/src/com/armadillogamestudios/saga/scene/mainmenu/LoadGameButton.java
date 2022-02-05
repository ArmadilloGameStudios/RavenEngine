package com.armadillogamestudios.saga.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class LoadGameButton
        extends UITextButton<MainMenuScene> {

    public LoadGameButton(MainMenuScene scene) {
        super(scene, "Continue", "button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().onLoadGameClick();
    }
}
