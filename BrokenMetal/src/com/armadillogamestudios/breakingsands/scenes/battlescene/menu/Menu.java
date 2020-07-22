package com.armadillogamestudios.breakingsands.scenes.battlescene.menu;

import com.armadillogamestudios.engine2d.ui.container.UICenterContainer;
import com.armadillogamestudios.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;

public class Menu extends UICenterContainer<BattleScene> {

    public Menu(BattleScene scene) {
        super(scene);

        Menu menu = this;
        MenuButton resumeButton = new MenuButton(getScene(), "resume") {
            @Override
            public void handleMouseClick() {
                menu.setVisibility(false);
                menu.getScene().setPaused(false);
            }
        };
        resumeButton.load();
        addChild(resumeButton);

        MenuButton mainMenuButton = new MenuButton(getScene(), "main menu") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().prepTransitionScene(new MainMenuScene(getScene().getGame()));
            }
        };
        mainMenuButton.load();
        addChild(mainMenuButton);

        MenuButton exitButton = new MenuButton(getScene(), "exit") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().exit();
            }
        };
        exitButton.load();
        addChild(exitButton);

        pack();
    }
}
