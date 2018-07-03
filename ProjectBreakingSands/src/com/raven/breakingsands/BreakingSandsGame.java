package com.raven.breakingsands;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.launcher.GameLauncher;
import com.raven.engine2d.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public class BreakingSandsGame extends Game<BreakingSandsGame> {

    public static void main(String[] args) {
        GameEngine.Launch(new BreakingSandsGame());
        System.out.println("Lunched");
    }

    @Override
    public void setup() {
    }

    @Override
    public void breakdown() {
        setRunning(false);
    }

    @Override
    public Scene loadInitialScene() {
        return new MainMenuScene(this);
    }

    @Override
    public String getTitle() {
        return "Breaking Sands";
    }

    @Override
    public String getMainDirectory() {
        return "ProjectBreakingSands";
    }

    @Override
    public boolean saveGame() {
        List<GameDataTable> gdtToSave = new ArrayList<>();

        Scene scene = getCurrentScene();

        if (scene instanceof BattleScene) {
            gdtToSave.add(new GameDataTable("current_save", ((BattleScene) getCurrentScene())));

            System.out.println(gdtToSave.get(0));

            return saveDataTables(gdtToSave);
        } else {
            return false;
        }
    }

    @Override
    public boolean loadGame() {
        List<GameData> currentSave = loadSavedGameData("current_save");
        if (currentSave != null && currentSave.size() > 0) {
            prepTransitionScene(new BattleScene(this, loadSavedGameData("current_save").get(0)));
            return true;
        } else {
            return false;
        }
    }
}
