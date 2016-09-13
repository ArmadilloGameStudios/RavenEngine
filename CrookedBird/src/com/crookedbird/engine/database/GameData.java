package com.crookedbird.engine.database;

import java.util.List;
import java.util.Map;

public class GameData {
	private Map<String, GameData> vals;
	private List<GameData> list;
	private Integer integer;
	private boolean bool;
	private boolean isBool;
	private String str;

	// Constructors
	public GameData(Map<String, GameData> vals) {
		this.vals = vals;
	}

	public GameData(List<GameData> list) {
		this.list = list;
	}

	public GameData(boolean bool) {
		this.isBool = true;
		this.bool = bool;
	}

	public GameData(String str) {
		this.str = str;
	}

	public GameData(int integer) {
		this.integer = new Integer(integer);
	}

	// Get
	public Map<String, GameData> getValues() {
		return vals;
	}

	public boolean isData() {
		return vals != null;
	}

	public Map<String, GameData> getData() {
		return vals;
	}
	
	public GameData getData(String prop) {
		return vals.get(prop.toLowerCase());
	}

	public boolean isList() {
		return list != null;
	}

	public List<GameData> getList() {
		return list;
	}
	
	public boolean isString() {
		return str != null;
	}
	
	public String getString() {
		return str;
	}
	
	public boolean isBoolean() {
		return isBool;
	}

	public boolean getBoolean() {
		return bool;
	}
	
	public boolean isInteger() {
		return integer != null;
	}
	
	public int getInteger() {
		return integer;
	}
	
	@Override
	public String toString() {
		if (isBoolean()) {
			return String.valueOf(this.getBoolean());
		} else if (isInteger()) {
			return String.valueOf(getInteger());
		} else if (isString()) {
			return String.format("\"%1$s\"", getString());
		} else if (this.isData()) {
			return String.valueOf(vals);
		} else if (this.isList()) {
			return String.valueOf(list);
		}
				
		return super.toString();
	}
}
