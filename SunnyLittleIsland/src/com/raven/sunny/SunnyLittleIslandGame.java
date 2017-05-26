package com.raven.sunny;

import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.scene.Scene;
import com.raven.sunny.scenes.RandomScene;

/**
 * Created by cookedbird on 5/8/17.
 */
public class SunnyLittleIslandGame extends Game {

    public static void main(String[] args) {
        GameEngine.Launch(new SunnyLittleIslandGame());
        System.out.println("Lunched");
    }

    @Override
    public void setup() {

    }

    @Override
    public void breakdown() {

    }

    @Override
    public Scene loadInitialScene() {
        return new RandomScene(this);
    }

    @Override
    public int getWidth() {
        return 1424;
    }

    @Override
    public int getHeight() {
        return 856;
    }

    @Override
    public String getTitle() {
        return "Sunny Little Island";
    }

    @Override
    public String getMainDirectory() {
        return "SunnyLittleIsland";
    }
}
