package com.armadillogamestudios.crashlanding.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class CreditsButton
        extends UITextButton<CrashLandingMainMenuScene> {

    public CreditsButton(CrashLandingMainMenuScene scene) {
        super(scene, "credits", "button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().onCreditsClick();
    }
}