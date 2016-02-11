package com.crookedbird.engine.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class GameDatabase {
	static private final String JDBC_DRIVER = "org.sqlite.JDBC";
	static private final String DB_NAME = "tms";
	static private final String DB_URL = "jdbc:sqlite:" + DB_NAME + ".db";

	private Dictionary<String, GameDataTable> tables = new Hashtable<String, GameDataTable>();

	public List<GameDataTable> getTables() {
		return Collections.list(tables.elements());
	}

	public GameDataTable getTable(String name) {
		return tables.get(name);
	}

	public boolean load() {
		Connection conn = null;
		Statement stmt = null;

		try {

			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to database " + DB_NAME + ".db...");
			conn = DriverManager.getConnection(DB_URL);

			stmt = conn.createStatement();

			/*
			 * File f = new File(DB_NAME + ".db"); if (f.exists()) f.delete();
			 * // test String sql =
			 * "CREATE TABLE Weapon (ID   INTEGER PRIMARY KEY ASC, " +
			 * " Name TEXT, Damage INTEGER )"; stmt.executeUpdate(sql);
			 * 
			 * sql =
			 * "INSERT INTO Weapon ( Name , Damage ) VALUES ( 'Gun', 10 )";
			 * stmt.executeUpdate(sql);
			 * 
			 * sql =
			 * "INSERT INTO Weapon ( Name , Damage ) VALUES ( 'Gun V2', 11 )";
			 * stmt.executeUpdate(sql);
			 * 
			 * sql = "CREATE TABLE Armor (ID INTEGER PRIMARY KEY ASC, " +
			 * " Name TEXT, Protection INTEGER )"; stmt.executeUpdate(sql);
			 * 
			 * sql =
			 * "INSERT INTO Armor ( Name , Protection ) VALUES ( 'Fur', 12 )";
			 * stmt.executeUpdate(sql);
			 * 
			 * sql =
			 * "INSERT INTO Armor ( Name , Protection ) VALUES ( 'Cloth', 2 )";
			 * stmt.executeUpdate(sql);
			 */

			// Set up Result variables
			ResultSet rs;
			ResultSetMetaData rsmd;

			// Get Tables
			rs = stmt
					.executeQuery("SELECT name FROM sqlite_master WHERE type='table'");
			rsmd = rs.getMetaData();

			while (rs.next()) {
				tables.put(rs.getString(1), new GameDataTable(rs.getString(1)));
			}

			// Populate Tables
			Enumeration<GameDataTable> tbs = tables.elements();
			while (tbs.hasMoreElements()) {
				GameDataTable table = tbs.nextElement();
				rs = stmt.executeQuery("SELECT * FROM " + table.getName());
				rsmd = rs.getMetaData();

				int colCount = rsmd.getColumnCount();

				List<String> colNames = new ArrayList<String>();
				for (int i = 1; i < colCount + 1; i++) {
					colNames.add(rsmd.getColumnName(i));
				}

				table.setColumns(colNames);

				while (rs.next()) {
					List<Object> vals = new ArrayList<Object>();
					for (int i = 1; i < colCount + 1; i++) {
						vals.add(rs.getObject(i));
					}

					table.addRow(vals);
				}
			}

			System.out.println("Database loaded successfully...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return true;
	}

	public void save() {

		Connection conn = null;
		Statement stmt = null;

		try {
			File f = new File(DB_NAME + ".db");
			File fb = new File(DB_NAME + "-backup.db");
			if (fb.exists())
				fb.delete();
			f.renameTo(fb);

			Class.forName(JDBC_DRIVER);

			System.out.println("Connecting to database " + DB_NAME + ".db...");
			conn = DriverManager.getConnection(DB_URL);

			stmt = conn.createStatement();

			String sql;

			// make tables
			for (GameDataTable t : this.getTables()) {

				// create table
				sql = "CREATE TABLE " + t.getName()
						+ " (ID INTEGER PRIMARY KEY ASC ";

				for (int i = 1; i < t.getColumns().size(); i++) {
					sql += ", " + t.getColumns().get(i) + " TEXT ";
				}

				sql += " ); ";

				System.out.println(sql);
				stmt.executeUpdate(sql);

				// add data
				for (GameDataRow r : t.getRows()) {

					sql = "INSERT INTO " + t.getName() + " ( ";

					for (int i = 1; i < t.getColumns().size(); i++) {
						if (i != 1) {
							sql += ", ";
						}
						sql += t.getColumns().get(i);
					}

					sql += " ) VALUES ( ";

					for (int i = 1; i < r.getValues().size(); i++) {
						if (i != 1) {
							sql += ", ";
						}
						sql += "'" + r.getValues().get(i) + "'";
					}

					sql += " );";

					System.out.println(sql);
					stmt.executeUpdate(sql);
				}
			}

			System.out.println("Database saved successfully...");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
	}

	public GameDataTable newTable(String name) {
		GameDataTable t = new GameDataTable(name);

		List<String> colNames = new ArrayList<String>();
		colNames.add("ID");

		t.setColumns(colNames);

		tables.put(name, t);

		return t;
	}
	
	public GameDataTable deleteTable(String name) {
		return tables.remove(name);
	}
}