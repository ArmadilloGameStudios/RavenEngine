package com.armadillogamestudios.engine2d.database;

import com.armadillogamestudios.engine2d.GameProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class GameDatabase {
    //<editor-fold> public methods
    private Map<String, GameDataTable> tables = new HashMap<>();

    private static GameDatabase gameDatabase;

    public GameDatabase() {
        gameDatabase = this;
    }

    public List<GameDataTable> getTables() {
        return new ArrayList<>(tables.values());
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
                String name = f.getName().split("_|\\.")[0];


                GameDataTable t;
                if (!tables.containsKey(name)) {
                    tables.put(name, t = new GameDataTable(name));
                } else {
                    t = tables.get(name);
                }

                // Populate Table
                populateTable(t, f);
            } else {
                loopThroughFiles(f);
            }
        }
    }

    private void populateTable(GameDataTable table, File file) {
        try {
            table.addAll(GameDataReader.readFile(Paths.get(file.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Error loading: " + file.getName());
            e.printStackTrace();
        }
    }
    //</editor-fold>

    public static GameDataTable all(String table) {
        return gameDatabase.getTable(table);
    }
}