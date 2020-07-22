package com.armadillogamestudios.breakingsands.scenes.mainmenuscene;

import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.ui.UITextButton;

import java.util.Random;

public class NewGameButton
        extends UITextButton<MainMenuScene> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "new game", "sprites/button.png", "mainbutton");
    }

    @Override
    public void handleMouseClick() {
        BrokenMetalGame game = getScene().getGame();

        Random r = new Random();
        GameDataList records = getScene().getGame().loadRecords();

        int id;
        boolean found;

        do {
            id = r.nextInt();

            int finalId = id;

            found = records.stream().anyMatch(rec -> finalId == rec.getInteger("id"));
        } while (found);

        game.setGameID(id);

        game.prepTransitionScene(new BattleScene(game, null, 1));
    }
}
