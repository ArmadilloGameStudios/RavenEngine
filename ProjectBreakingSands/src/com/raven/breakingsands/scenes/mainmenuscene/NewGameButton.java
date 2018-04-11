package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.UIButton;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataQuery;
import com.raven.engine2d.ui.UIContainer;

public class NewGameButton
        extends UIButton<MainMenuScene, UIContainer<MainMenuScene>> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, GameEngine.getEngine().getGameDatabase().getTable("buttons").queryFirst(
                new GameDataQuery() {
                    @Override
                    public boolean matches(GameData row) {
                        return row.getString("name").equals("new game");
                    }
                }
        ));
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
