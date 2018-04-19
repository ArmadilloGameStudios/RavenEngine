package com.raven.engine2d.database;

import com.raven.engine2d.GameEngine;
import com.raven.engine2d.GameProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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

			// Find Tables
			for (File f : dataDirectory.listFiles()) {
				if (f.isFile()) {
					GameDataTable t = new GameDataTable(f.getName());
					tables.put(f.getName(), t);

					// Populate Table
					populateTable(t, f);
				}
			}

			System.out.println(name + " loaded successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	//</editor-fold>

	//<editor-fold> private methods
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
				return row.asData(prop).asString().equals(value);
			}
		});
	}

	public static GameData queryFirst(String table, String prop, String value) {
		return GameEngine.getEngine().getGameDatabase().getTable(table).queryFirst(new GameDataQuery() {
			@Override
			public boolean matches(com.raven.engine2d.database.GameData row) {
				return row.asData(prop).asString().equals(value);
			}
		});
	}

	public static GameData queryRandom(String table, String prop, String value) {
		return GameEngine.getEngine().getGameDatabase().getTable(table).queryRandom(new GameDataQuery() {
			@Override
			public boolean matches(com.raven.engine2d.database.GameData row) {
				return row.asData(prop).asString().equals(value);
			}
		});
	}


	public static com.raven.engine2d.database.GameDataList all(String table) {
		return com.raven.engine2d.GameEngine.getEngine().getGameDatabase().getTable(table);
	}
    //</editor-fold>
}