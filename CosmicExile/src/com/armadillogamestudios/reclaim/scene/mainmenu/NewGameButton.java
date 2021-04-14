package com.armadillogamestudios.reclaim.scene.mainmenu;

import com.armadillogamestudios.engine2d.ui.UITextButton;
import com.armadillogamestudios.engine2d.worldobject.Highlight;

public class NewGameButton
        extends UITextButton<CosmicExileMainMenuScene> {

    public NewGameButton(CosmicExileMainMenuScene scene) {
        super(scene, "new game", "button.png", "mainbutton");
        setTextHighlight(new Highlight(0f, 0f, 0f, 1f));
    }

    @Override
    public void handleMouseClick() {
        getScene().onNewGameClick();
    }
}
