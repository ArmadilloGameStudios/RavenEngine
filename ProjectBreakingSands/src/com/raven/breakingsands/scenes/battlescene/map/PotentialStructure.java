package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDataTable;

public class PotentialStructure {
    private GameData gdStructure;
    private GameData gdEntrance;
    private boolean terminal = false;

    public PotentialStructure(StructureEntrance existingEntrance, GameData gdConnection) {
        GameDataTable structures = GameEngine.getEngine().getGameDatabase().getTable("structure");

        if (gdConnection.has("terminal"))
            terminal = gdConnection.getBoolean("terminal");

        structures.stream()
                .filter(s ->
                        s.getString("name").equals(gdConnection.getString("name")))
                .findFirst()
                .ifPresent(s -> gdStructure = s);

        if (gdStructure != null) {
            gdStructure.getList("entrance").stream()
                    .filter(e ->
                            e.getString("name").equals(gdConnection.getString("entrance")))
                    .findFirst()
                    .ifPresent(e -> gdEntrance = e);
        }

    }

    public GameData getEntrance() {
        return gdEntrance;
    }

    public GameData getStructure() {
        return gdStructure;
    }

    public boolean isTerminal() {
        return terminal;
    }
}
