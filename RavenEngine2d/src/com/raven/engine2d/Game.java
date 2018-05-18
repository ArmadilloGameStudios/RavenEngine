package com.raven.engine2d;

import com.raven.engine2d.graphics2d.GameWindow;
import com.raven.engine2d.scene.Scene;

import javax.sound.sampled.Clip;
import java.util.List;

public abstract class Game<G extends Game> {
    private GameEngine<G> engine;

    private Scene<G> currentScene;
    private Scene<G> readyTransitionScene;

    private Clip song;

    private boolean isRunning;

    public Game() {
        isRunning = true;
    }

    public GameEngine<G> getEngine() {
        return engine;
    }

    public void setEngine(GameEngine<G> engine) {
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


        scene.enterScene();
        currentScene = scene;
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
            song.start();
        }
    }

    abstract public void setup();

    abstract public void breakdown();

    abstract public Scene loadInitialScene();

    abstract public String getTitle();

    abstract public String getMainDirectory();

    abstract public boolean saveGame();

    abstract public boolean loadGame();
}
