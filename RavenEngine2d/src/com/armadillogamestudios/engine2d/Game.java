package com.armadillogamestudios.engine2d;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDataList;
import com.armadillogamestudios.engine2d.database.GameDataReader;
import com.armadillogamestudios.engine2d.database.GameDataTable;
import com.armadillogamestudios.engine2d.graphics2d.GameWindow;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.worldobject.GameObject;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Game<G extends Game<G>> {
    private GameEngine<G> engine;

    private Scene<G> currentScene;
    private Scene<G> readyTransitionScene;

    private Clip song;

    private boolean isRunning;

    private ExecutorService threadPool = Executors.newSingleThreadExecutor();

    public Game() {
        isRunning = true;

        GameProperties.setMainDirectory(getMainDirectory());

        loadSettings();
    }

    public GameEngine<G> getEngine() {
        return engine;
    }

    void setEngine(GameEngine<G> engine) {
        this.engine = engine;
    }

    final public Scene getCurrentScene() {
        return currentScene;
    }

    final public void draw(GameWindow window) {
        currentScene.draw(window);
    }

    final public void update(float deltaTime) {
        currentScene.update(deltaTime);

        if (readyTransitionScene != null) {
            transitionScene(readyTransitionScene);
        }
    }

    final protected void transitionScene(Scene<G> scene) {
        if (currentScene != null) {
            currentScene.exitScene();
        }

        GameObject.resetObjectIDs();

        currentScene = scene;
        scene.enterScene();
        readyTransitionScene = null;
    }

    final public void prepTransitionScene(Scene<G> scene) {
        readyTransitionScene = scene;
    }

    final public boolean isRunning() {
        return isRunning;
    }

    final protected void setRunning(boolean running) {
        this.isRunning = running;
    }

    final public void exit() {
        currentScene.onExitScene();
        isRunning = false;
        breakdown();
    }

    final public void playSong(String name) {
        if (song != null)
            song.stop();

        song = engine.getAudioClip(name);

        if (song != null) {
            song.stop();
            song.setFramePosition(0);
            song.setMicrosecondPosition(0);

            // TODO shouldn't need to be done every time
            if (engine.changeSongVolume(GameProperties.getMusicVolume(), song)) {
                song.loop(-1);
                song.start();
            } else {
                song = null;
                System.out.println("Missing Audio Controls: " + name);
            }
        }
    }

    private void loadSettings() {
        GameData settings = loadGameData("settings.gd").get(0);

        settings.ifHas("music", s -> GameProperties.setMusicVolume(s.asInteger()));
        settings.ifHas("sfx", s -> GameProperties.setSFXVolume(s.asInteger()));
        settings.ifHas("width", s -> GameProperties.setDisplayWidth(s.asInteger()));
        settings.ifHas("height", s -> GameProperties.setDisplayHeight(s.asInteger()));
        settings.ifHas("scaling", s -> GameProperties.setScaling(s.asInteger()));
        settings.ifHas("vsync", s -> GameProperties.setVSync(s.asBoolean()));
        settings.ifHas("win_mode", s -> GameProperties.setWindowMode(s.asInteger()));
    }

    public void changeSongVolume(int value) {
        if (song != null)
            engine.changeSongVolume(value, song);
    }

    abstract public void setup();

    abstract public void breakdown();

    abstract public Scene<G> loadInitialScene();

    abstract public String getTitle();

    abstract public String getMainDirectory();

    abstract public boolean saveGame();

    protected void saveDataTablesThreaded(List<GameDataTable> gdtToSave) {
        threadPool.execute(() -> saveDataTables(gdtToSave, "save"));
    }

    private boolean saveDataTables(List<GameDataTable> gdtToSave, String folder) {
        boolean success = true;

        try {
            for (GameDataTable table : gdtToSave) {
                Path p = Paths.get(getMainDirectory(), folder, table.getName());
                File f = p.toFile();

                if (!f.getParentFile().exists())
                    f.getParentFile().mkdirs();

                if (f.exists())
                    Files.delete(p);

                Files.write(p, table.toFileString().getBytes(), StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    public void deleteDataTables(String name) {
        Path p = Paths.get(getMainDirectory(), name);

        Arrays.stream(p.toFile().listFiles()).forEach(File::delete);
    }

    protected void saveDataTableThreaded(GameDataTable gdtToSave) {
        threadPool.execute(() -> saveDataTable(gdtToSave));

    }

    private boolean saveDataTable(GameDataTable gdtToSave) {
        boolean success = true;

        try {
            Path p = Paths.get(getMainDirectory(), gdtToSave.getName());
            File f = p.toFile();

            if (f.exists())
                Files.delete(p);

            Files.write(p, gdtToSave.toFileString().getBytes(), StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    abstract public boolean loadGame();

    protected List<GameData> loadSavedGameData(String table) {
        Path savePath = Paths.get(getMainDirectory(), "save");

        try {
            return GameDataReader.readFile(savePath.resolve(table));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public GameDataList loadGameData(String file) {
        Path savePath = Paths.get(getMainDirectory());

        try {
            return GameDataReader.readFile(savePath.resolve(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new GameDataList();
    }
}
