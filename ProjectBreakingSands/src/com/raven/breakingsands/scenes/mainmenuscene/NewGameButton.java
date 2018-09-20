package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.ui.UITextButton;

import java.util.List;
import java.util.Random;

public class NewGameButton
        extends UITextButton<MainMenuScene> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "new game", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        Random r = new Random();
        GameDataList records = getScene().getGame().loadRecords();

        int id;
        boolean found;

        do {
            id = r.nextInt();

            int finalId = id;

            found = records.stream().anyMatch(rec -> finalId == rec.getInteger("id"));
        } while (found);

        System.out.println(id);
        game.setGameID(id);

        game.prepTransitionScene(new BattleScene(game, null, 1));
    }
}
