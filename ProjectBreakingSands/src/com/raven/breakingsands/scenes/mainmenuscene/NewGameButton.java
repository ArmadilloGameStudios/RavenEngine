package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.UIButton;
import com.raven.engine2d.ui.UIContainer;

public class NewGameButton
        extends UIButton<MainMenuScene, UIContainer<MainMenuScene>> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "New Game");
    }

    @Override
    public float getHeight() {
        return 75f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.newGame();

        game.prepTransitionScene(new BattleScene(game));
    }
}
