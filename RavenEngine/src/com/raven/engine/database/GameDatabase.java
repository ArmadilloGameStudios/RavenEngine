package com.raven.engine.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class GameDatabase {
	private Dictionary<String, GameDataTable> tables = new Hashtable<String, GameDataTable>();

	public List<GameDataTable> getTables() {
		return Collections.list(tables.elements());
	}

	public GameDataTable getTable(String name) {
		return tables.get(name);
	}

	public boolean load() {
		try {
			System.out.println("Connecting to database.");

			File dataDirectory = new File("TacticianMercSquad" + File.separator + "data");

			// Find Tables
			for (File f : dataDirectory.listFiles()) {
				if (f.isFile()) {
					GameDataTable t = new GameDataTable(f.getName());
					tables.put(f.getName(), t);

					// Populate Table
					populateTable(t, f);
				}
			}
			System.out.println("Database loaded successfully...");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private void populateTable(GameDataTable table, File file) {
		try {
			char[] contents = new String(Files.readAllBytes(Paths.get(file
					.getPath()))).toCharArray();

			for (GameData data : GameDataReader.readFile(contents)) {
				table.addRow(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}