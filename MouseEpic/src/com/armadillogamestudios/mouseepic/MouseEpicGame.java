package com.armadillogamestudios.mouseepic;

import com.armadillogamestudios.mouseepic.scenes.LoadWorldScene;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.scene.Scene;

public class MouseEpicGame extends Game<MouseEpicGame> {

    public static void main(String[] args) {
        GameProperties.setMainDirectory("MouseEpic");
        GameProperties.setVSync(true);
        GameProperties.setScaling(2);
        GameProperties.setDisplayHeight(640);
        GameProperties.setDisplayWidth(640);
        GameProperties.setWindowMode(1);
        GameProperties.setVSync(false);
        GameProperties.setUseResolution(false);

        GameEngine.Launch(new MouseEpicGame());
    }

    @Override
    public void setup() {

    }

    @Override
    public void breakdown() {

    }

    @Override
    public Scene loadInitialScene() {
        return new LoadWorldScene(this);
    }

    @Override
    public String getTitle() {
        return "Mouse Epic";
    }

    @Override
    public String getMainDirectory() {
        return "MouseEpic";
    }

    @Override
    public boolean saveGame() {
        return false;
    }

    @Override
    public boolean loadGame() {
        return false;
    }
}
