package com.armadillogamestudios.crashlanding.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class ExitButton
        extends UITextButton<CrashLandingMainMenuScene> {

    public ExitButton(CrashLandingMainMenuScene scene) {
        super(scene, "exit", "button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().onExitClick();
    }
}
