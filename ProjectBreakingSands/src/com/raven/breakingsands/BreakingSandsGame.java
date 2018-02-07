package com.raven.breakingsands;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine.Game;
import com.raven.engine.GameEngine;
import com.raven.engine.database.GameData;
import com.raven.engine.database.GameDataList;
import com.raven.engine.database.GameDataReader;
import com.raven.engine.database.GameDataTable;
import com.raven.engine.launcher.GameLauncher;
import com.raven.engine.scene.Scene;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BreakingSandsGame extends Game<BreakingSandsGame> {

    public static void main(String[] args) {
        GameLauncher.Open(new BreakingSandsGame());
        System.out.println("Lunched");

    }

    private List<Character> characters = new ArrayList<>();

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
        boolean success = true;

        GameDataTable charGDL = new GameDataTable("characters", characters);

        Path charPath = Paths.get(getMainDirectory(), "save", charGDL.getName());

        File f = charPath.toFile();

        try {
            if (f.getParentFile().exists())
                f.getParentFile().mkdirs();

            if (!f.exists())
                f.createNewFile();

            Files.write(charPath, charGDL.toString().getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    @Override
    public boolean loadGame() {
        boolean success = true;

        Path savePath = Paths.get(getMainDirectory(), "save");

        GameDataTable charGDL = new GameDataTable("characters");

        try {
            for (GameData data : GameDataReader.readFile(savePath.resolve("characters"))) {
                charGDL.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        for (GameData gameData : charGDL) {
            characters.add(new Character(gameData));
        }

        return success;
    }
}
