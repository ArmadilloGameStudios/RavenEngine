package com.raven.breakingsands;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.launcher.GameLauncher;
import com.raven.engine2d.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BreakingSandsGame extends Game<BreakingSandsGame> {

    private static String mainDirectory = "ProjectBreakingSands";

    public static void main(String[] args) {
        GameData settings = loadSettingsGameData(mainDirectory).get(0);

        settings.ifHas("music", s -> GameProperties.setMusicVolume(s.asInteger()));
        settings.ifHas("sfx", s -> GameProperties.setSFXVolume(s.asInteger()));
        settings.ifHas("width", s -> GameProperties.setScreenWidth(s.asInteger()));
        settings.ifHas("height", s -> GameProperties.setScreenHeight(s.asInteger()));
        settings.ifHas("scaling", s -> GameProperties.setScaling(s.asInteger()));

        GameEngine.Launch(new BreakingSandsGame());
        System.out.println("Lunched");
    }

    @Override
    public void setup() {
        playSong("theme.wav");
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
        return mainDirectory;
    }

    @Override
    public boolean saveGame() {
        List<GameDataTable> gdtToSave = new ArrayList<>();

        Scene scene = getCurrentScene();

        if (scene instanceof BattleScene) {
            gdtToSave.add(new GameDataTable("current_save", ((BattleScene) scene)));

            Thread t = new Thread(() -> saveDataTables(gdtToSave));
            t.start();
            return true;
        } else {
            System.out.println("Saving Failed");
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
            System.out.println("Loading Failed");
            return false;
        }
    }

    @Override
    public boolean saveSettings() {
        HashMap<String, GameData> map = new HashMap<>();
        map.put("music", new GameData(GameProperties.getMusicVolume()));
        map.put("sfx", new GameData(GameProperties.getSFXVolume()));
        map.put("width", new GameData(GameProperties.getScreenWidth()));
        map.put("height", new GameData(GameProperties.getScreenHeight()));
        map.put("scaling", new GameData(GameProperties.getScaling()));

        GameDataTable settings = new GameDataTable("settings");
        settings.add(new GameData(map));

        return saveSettingsDataTables(settings);
    }
}
