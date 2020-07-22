package com.armadillogamestudios.breakingsands.scenes.battlescene.menu;

import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;
import com.armadillogamestudios.engine2d.ui.UITextButton;

public abstract class MenuButton extends UITextButton<BattleScene> {

    private static final String btnImgSrc = "sprites/button.png";

    public MenuButton(BattleScene scene, String text) {
        super(scene, text, btnImgSrc, "mainbutton");
    }
}
