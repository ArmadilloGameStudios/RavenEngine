package com.raven.breakingsands.scenes.battlescene.menu;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDCenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector4f;

public class Menu extends HUDCenterContainer<BattleScene> {

    private Vector4f color = new Vector4f(.25f, .25f, .25f, .5f);

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
        menu.addChild(resumeButton);

        MenuButton mainMenuButton = new MenuButton(getScene(), "Main Menu") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().prepTransitionScene(new MainMenuScene(getScene().getGame()));
            }
        };
        menu.addChild(mainMenuButton);

        MenuButton exitButton = new MenuButton(getScene(), "Exit") {
            @Override
            public void handleMouseClick() {
                getScene().getGame().exit();
            }
        };
        menu.addChild(exitButton);
    }

    @Override
    public Vector4f getColor() {
        return color;
    }
}
