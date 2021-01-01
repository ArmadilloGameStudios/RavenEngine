package com.raven.sunny;

import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.launcher.GameLauncher;
import com.raven.engine.scene.Scene;
import com.raven.sunny.scenes.RandomScene;

/**
 * Created by cookedbird on 5/8/17.
 */
public class SunnyLittleIslandGame extends Game {

    public static void main(String[] args) {
        GameLauncher.Open(new SunnyLittleIslandGame());
        System.out.println("Lunched");
    }

    @Override
    public void setup() {

    }

    @Override
    public void breakdown() {
        this.setRunning(false);
    }

    @Override
    public Scene loadInitialScene() {
        return new RandomScene();
    }

    @Override
    public String getTitle() {
        return "Little Island";
    }

    @Override
    public String getMainDirectory() {
        return "SunnyLittleIsland";
    }
}
