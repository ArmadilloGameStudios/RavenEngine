package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.util.math.Vector4f;

public class Menu extends UICenterContainer<BattleScene> {

    public Menu(BattleScene scene) {
        super(scene);

        Menu menu = this;
        MenuButton resumeButton = new MenuButton(getScene(), "Resume") {
            @Override
            public void handleMouseClick() {
                menu.setVisibility(false);
                menu.getScene().setPaused(false);
            }
        };
        addChild(resumeButton);
        MenuButton mainMenuButton = new MenuButton(getScene(), "Main Menu") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().prepTransitionScene(new MainMenuScene(getScene().getGame()));
            }
        };
        addChild(mainMenuButton);

        MenuButton exitButton = new MenuButton(getScene(), "Exit") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().saveGame();
                getScene().getGame().exit();
            }
        };
        addChild(exitButton);

        pack();
    }
}
