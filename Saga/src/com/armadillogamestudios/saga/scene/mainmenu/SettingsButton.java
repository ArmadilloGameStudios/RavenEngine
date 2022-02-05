package com.armadillogamestudios.saga.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class SettingsButton
        extends UITextButton<MainMenuScene> {

    public SettingsButton(MainMenuScene scene) {
        super(scene, "settings", "button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().onSettingsClick();
    }
}
