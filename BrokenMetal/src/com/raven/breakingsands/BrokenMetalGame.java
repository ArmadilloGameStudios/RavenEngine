package com.raven.breakingsands;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.splashscreenscene.SplashScreenScene;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.database.GameDatable;
import com.raven.engine2d.scene.Scene;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BrokenMetalGame extends Game<BrokenMetalGame> {

    private static String mainDirectory = "BrokenMetal";
    private int gameId;

    public static void main(String[] args) {
        GameEngine.Launch(new BrokenMetalGame());
        System.out.println("Lunched");
    }

    public BrokenMetalGame() {
        GameData settings = loadGameData("settings").get(0);

        settings.ifHas("music", s -> GameProperties.setMusicVolume(s.asInteger()));
        settings.ifHas("sfx", s -> GameProperties.setSFXVolume(s.asInteger()));
        settings.ifHas("width", s -> GameProperties.setScreenWidth(s.asInteger()));
        settings.ifHas("height", s -> GameProperties.setScreenHeight(s.asInteger()));
        settings.ifHas("scaling", s -> GameProperties.setScaling(s.asInteger()));
        settings.ifHas("vsync", s -> GameProperties.setVSync(s.asBoolean()));
    }

    @Override
    public void setup() {
        playSong("hf.wav");
    }

    @Override
    public void breakdown() {
        setRunning(false);
    }

    @Override
    public Scene loadInitialScene() {
        return new SplashScreenScene(this);
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
            saveRecords();
            gdtToSave.add(new GameDataTable("current_save", ((BattleScene) scene)));
            saveDataTablesThreaded(gdtToSave);
            return true;
        } else {
            System.out.println("Saving Failed");
            return false;
        }
    }

    public void deleteSaveGame() {
        deleteDataTables("save");
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

    public void saveSettings() {
        HashMap<String, GameData> map = new HashMap<>();
        map.put("music", new GameData(GameProperties.getMusicVolume()));
        map.put("sfx", new GameData(GameProperties.getSFXVolume()));
        map.put("width", new GameData(GameProperties.getScreenWidth()));
        map.put("height", new GameData(GameProperties.getScreenHeight()));
        map.put("scaling", new GameData(GameProperties.getScaling()));
        map.put("vsync", new GameData(GameProperties.getVSync()));

        GameDataTable settings = new GameDataTable("settings");
        settings.add(new GameData(map));

        saveDataTableThreaded(settings);
    }

    public GameDataList loadRecords() {
        return loadGameData("records");
    }

    private void saveRecords() {
        BattleScene scene = ((BattleScene) this.getCurrentScene());
        GameDataList records = loadRecords();

        Optional<GameData> existing = records.stream()
                .filter(d -> d.getInteger("id") == gameId)
                .findFirst();

        if (existing.isPresent()) {
            GameData data = existing.get();

            Map<String, GameData> map = data.asMap();
            map.put("floor", new GameData(scene.getDifficulty()));
            DateFormat df = new SimpleDateFormat("M/dd/yyyy");
            map.put("date", new GameData(df.format(new Date())));
        } else {
            Map<String, GameData> map = new HashMap<>();
            map.put("floor", new GameData(scene.getDifficulty()));
            DateFormat df = new SimpleDateFormat("M/dd/yyyy");
            map.put("date", new GameData(df.format(new Date())));
            map.put("id", new GameData(gameId));
            records.add(new GameData(map));
        }


        this.saveDataTableThreaded(new GameDataTable("records", (List<? extends GameDatable>)records));
    }

    public void setGameID(int id) {
        gameId = id;
    }

    public int getGameID() {
        return gameId;
    }
}
