package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.ui.UITextButton;

import java.util.List;

public class NewGameButton
        extends UITextButton<MainMenuScene> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "new game", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.prepTransitionScene(new BattleScene(game, (List<Pawn>) null, 1));
    }
}
