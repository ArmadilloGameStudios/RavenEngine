package com.raven.engine2d;

import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataList;
import com.raven.engine2d.database.GameDataReader;
import com.raven.engine2d.database.GameDataTable;
import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.worldobject.GameObject;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

public abstract class Game<G extends Game<G>> {
    private GameEngine<G> engine;

    private Scene<G> currentScene;
    private Scene<G> readyTransitionScene;

    private Clip song;

    private boolean isRunning;

    public Game() {
        isRunning = true;

        GameProperties.setMainDirectory(getMainDirectory());
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

    final protected void transitionScene(Scene scene) {
        if (currentScene != null) {
            currentScene.exitScene();
        }

        GameObject.resetObjectIDs();

        currentScene = scene;
        scene.enterScene();
        readyTransitionScene = null;
    }

    final public void prepTransitionScene(Scene scene) {
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

    public void changeSongVolume(int value) {
        if (song != null)
            engine.changeSongVolume(value, song);
    }

    abstract public void setup();

    abstract public void breakdown();

    abstract public Scene loadInitialScene();

    abstract public String getTitle();

    abstract public String getMainDirectory();

    abstract public boolean saveGame();

    protected boolean saveDataTables(List<GameDataTable> gdtToSave) {
        boolean success = true;

        try {
            for (GameDataTable table : gdtToSave) {
                Path p = Paths.get(getMainDirectory(), "save", table.getName());
                File f = p.toFile();

                if (f.getParentFile().exists())
                    f.getParentFile().mkdirs();

                if (!f.exists())
                    f.createNewFile();

                Files.write(p, table.toFileString().getBytes(), StandardOpenOption.CREATE);
            }
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

    abstract public boolean saveSettings();

    protected boolean saveSettingsDataTables(GameDataTable table) {
        boolean success = true;

        try {
            Path p = Paths.get(getMainDirectory(), "settings");
            File f = p.toFile();

            if (f.getParentFile().exists())
                f.getParentFile().mkdirs();

            if (!f.exists())
                f.createNewFile();

            Files.write(p, table.toFileString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    protected static GameDataList loadSettingsGameData(String mainDirectory) {
        Path savePath = Paths.get(mainDirectory);

        try {
            return GameDataReader.readFile(savePath.resolve("settings"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new GameDataList();
    }
}
