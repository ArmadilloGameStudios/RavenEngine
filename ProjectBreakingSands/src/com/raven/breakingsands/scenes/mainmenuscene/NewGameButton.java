package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine2d.ui.UIButton;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.ui.UIContainer;
import com.raven.engine2d.ui.UITextButton;

public class NewGameButton
        extends UITextButton<MainMenuScene> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "new game", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.newGame();

        game.prepTransitionScene(new BattleScene(game));
    }
}
