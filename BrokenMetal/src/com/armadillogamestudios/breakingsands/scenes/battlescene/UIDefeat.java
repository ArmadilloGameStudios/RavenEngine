package com.armadillogamestudios.breakingsands.scenes.battlescene;

import com.armadillogamestudios.engine2d.ui.container.UICenterContainer;
import com.armadillogamestudios.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.UITextButton;

public class UIDefeat extends UICenterContainer<BattleScene> {

    public UIDefeat(BattleScene scene) {
        super(scene);

        UIImage<BattleScene> imgGameOver = new UIImage<>(scene, 200, 58, "sprites/gameover.png");
        addChild(imgGameOver);

        UITextButton<BattleScene> btnMenu = new UITextButton<BattleScene>(scene, "menu", "sprites/button.png", "mainbutton") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().prepTransitionScene(new MainMenuScene(getScene().getGame()));
//                getScene().getGame().deleteSaveGame();
            }
        };
        btnMenu.load();
        addChild(btnMenu);

        pack();

        btnMenu.setZ(.02f);
        btnMenu.setY(5);
    }
}
