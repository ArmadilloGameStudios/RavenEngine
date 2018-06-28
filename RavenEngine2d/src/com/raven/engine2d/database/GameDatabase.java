package com.raven.engine2d.database;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class GameDatabase {
    //<editor-fold> public methods
    private Dictionary<String, GameDataTable> tables = new Hashtable<String, GameDataTable>();

    public List<GameDataTable> getTables() {
        return Collections.list(tables.elements());
    }

    public GameDataTable getTable(String name) {
        return tables.get(name);
    }

    public boolean load(String name) {
        try {
            System.out.println("Loading to " + name);

            File dataDirectory = new File(GameProperties.getMainDirectory() + File.separator + name);
            loopThroughFiles(dataDirectory);
            // Find Tables

            System.out.println(name + " loaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    //</editor-fold>

    //<editor-fold> private methods
    private void loopThroughFiles(File base) {
        for (File f : base.listFiles()) {
            if (f.isFile()) {
                GameDataTable t = new GameDataTable(f.getName());
                tables.put(f.getName(), t);

                // Populate Table
                populateTable(t, f);
            } else {
                loopThroughFiles(f);
            }
        }
    }

    private void populateTable(GameDataTable table, File file) {
        try {
            for (GameData data : GameDataReader.readFile(Paths.get(file.getPath()))) {
                table.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //</editor-fold>

    //<editor-fold> static methods
    public static GameDataList queryAll(String table, String prop, String value) {
        return GameEngine.getEngine().getGameDatabase().getTable(table).queryAll(new GameDataQuery() {
            @Override
            public boolean matches(com.raven.engine2d.database.GameData row) {
                return row.getData(prop).asString().equals(value);
            }
        });
    }

    public static GameData queryFirst(String table, String prop, String value) {
        return GameEngine.getEngine().getGameDatabase().getTable(table).queryFirst(new GameDataQuery() {
            @Override
            public boolean matches(com.raven.engine2d.database.GameData row) {
                return row.getData(prop).asString().equals(value);
            }
        });
    }

    public static GameData queryRandom(String table, String prop, String value, Random r) {
        return GameEngine.getEngine().getGameDatabase().getTable(table).queryRandom(r, new GameDataQuery() {
            @Override
            public boolean matches(GameData row) {
                return row.getData(prop).asString().equals(value);
            }
        });
    }


    public static GameDataTable all(String table) {
        return GameEngine.getEngine().getGameDatabase().getTable(table);
    }
    //</editor-fold>
}