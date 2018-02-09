package com.raven.breakingsands;

import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.breakingsands.scenes.missionselectscene.MissionSelectScene;
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
    private List<Mission> missions = new ArrayList<>();
    private Mission activeMission = null;

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

        List<GameDataTable> gdtToSave = new ArrayList<>();

        gdtToSave.add(new GameDataTable("characters", characters));
        gdtToSave.add(new GameDataTable("missions", missions));

        try {
            for (GameDataTable table : gdtToSave) {
                Path p = Paths.get(getMainDirectory(), "save", table.getName());
                File f = p.toFile();

                if (f.getParentFile().exists())
                    f.getParentFile().mkdirs();

                if (!f.exists())
                    f.createNewFile();

                Files.write(p, table.toString().getBytes(), StandardOpenOption.CREATE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }

    @Override
    public boolean loadGame() {
        boolean success = true;

        characters.clear();
        missions.clear();

        Path savePath = Paths.get(getMainDirectory(), "save");

        try {
            for (GameData data : GameDataReader.readFile(savePath.resolve("characters"))) {
                characters.add(new Character(data));
            }

            for (GameData data : GameDataReader.readFile(savePath.resolve("missions"))) {
                missions.add(new Mission(data));
            }
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }

        prepTransitionScene(new MissionSelectScene(this));

        return success;
    }

    public void newGame() {
        characters.clear();
        missions.clear();

        // starting characters
        Character character = new Character();
        character.setName("Jotlin");
        character.setTitle("Captain");
        character.setExp(6);
        character.setLevel(3);
        character.setHitPoints(12);
        character.setMovement(80);
        character.setEvasion(3);
        characters.add(character);

        character = new Character();
        character.setName("Admus");
        character.setTitle("Recruit");
        character.setExp(0);
        character.setLevel(0);
        character.setHitPoints(10);
        character.setMovement(80);
        character.setEvasion(3);
        characters.add(character);

        character = new Character();
        character.setName("Ellet");
        character.setTitle("Recruit");
        character.setExp(0);
        character.setLevel(0);
        character.setHitPoints(6);
        character.setMovement(80);
        character.setEvasion(4);
        characters.add(character);
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public void setActiveMission(Mission activeMission) {
        this.activeMission = activeMission;
    }

    public void resolveActiveMission(boolean success) {
        missions.remove(activeMission);
    }
}
