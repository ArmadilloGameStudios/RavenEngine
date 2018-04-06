package com.raven.engine2d.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameDataReader {
	static enum ReadDataState {
		Open, FindProperty, ReadProperty, FindValue, ReadValue
	}

	static enum ValueType {
		GameData, String, Integer, Boolean, List
	}

	private static int i = 0;

	public static List<com.raven.engine2d.database.GameData> readFile(Path path) throws IOException {
	    return readFile(new String(Files.readAllBytes(path)).toCharArray());
    }

	public static List<com.raven.engine2d.database.GameData> readFile(char[] chars) {
		i = 0;

		List<com.raven.engine2d.database.GameData> data = new ArrayList<com.raven.engine2d.database.GameData>();

		while (i < chars.length) {
			com.raven.engine2d.database.GameData d = readData(chars);

			if (d != null)
				data.add(d);
		}

		for (com.raven.engine2d.database.GameData d : data) {
			System.out.println(d);
		}

		return data;
	}

	private static com.raven.engine2d.database.GameData readData(char[] chars) {
		return readData(chars, false);
	}

	private static com.raven.engine2d.database.GameData readData(char[] chars, boolean array) {
		Map<String, com.raven.engine2d.database.GameData> data = new HashMap<String, com.raven.engine2d.database.GameData>();
		com.raven.engine2d.database.GameDataList list = new com.raven.engine2d.database.GameDataList();

		ReadDataState state = ReadDataState.Open;
		ValueType type = ValueType.GameData;

		String prop = "";
		String value = "";
		com.raven.engine2d.database.GameData d;

		boolean done = false;
		while (!done) {
			switch (state) {
			case Open:
				switch (chars[i]) {
				case '{':
					state = ReadDataState.FindProperty;
					break;
				case '[':
					state = ReadDataState.FindValue;
					array = true; // Unnecessary?
					break;
				}
				break;
			case FindProperty:
				if (Character.isAlphabetic(chars[i])) {
					prop = String.valueOf(chars[i]);
					state = ReadDataState.ReadProperty;
				} else if ('}' == chars[i]) {
					done = true;
				}
				break;
			case ReadProperty:
				if (!Character.isAlphabetic(chars[i]) || chars[i] == ':') {
					state = ReadDataState.FindValue;
				} else {
					prop += chars[i];
				}
				break;
			case FindValue:
				if ('"' == chars[i]) {
					state = ReadDataState.ReadValue;
					type = ValueType.String;
					value = "";
				} else if (Character.isDigit(chars[i]) || chars[i] == '-') {
					state = ReadDataState.ReadValue;
					type = ValueType.Integer;
					value = String.valueOf(chars[i]);
				} else if (Character.isAlphabetic(chars[i])) {
					state = ReadDataState.ReadValue;
					type = ValueType.Boolean;
					value = String.valueOf(chars[i]);
				} else if ('{' == chars[i]) {
					state = ReadDataState.FindProperty;
					type = ValueType.GameData;
					if (array) {
						list.add(readData(chars));
						state = ReadDataState.FindValue;
					} else {
						data.put(prop.toLowerCase(), readData(chars));
						state = ReadDataState.FindProperty;
					}
					i--;
				} else if ('[' == chars[i]) {
					type = ValueType.List;
					if (array) {
						list.add(readData(chars, true));
						state = ReadDataState.FindValue;
					} else {
						data.put(prop.toLowerCase(), readData(chars, true));
						state = ReadDataState.FindProperty;
					}
					i--;
				} else if (']' == chars[i]) {
					done = true;
				}
				break;
			case ReadValue:
				switch (type) {
				case Boolean:
					if (Character.isWhitespace(chars[i]) || chars[i] == ',') {
						if (array) {
							list.add(new com.raven.engine2d.database.GameData(Boolean.parseBoolean(value)));
							state = ReadDataState.FindValue;
						} else {
							data.put(prop.toLowerCase(),
									new com.raven.engine2d.database.GameData(Boolean.parseBoolean(value)));
							state = ReadDataState.FindProperty;
						}
					} else if (chars[i] == '}' || chars[i] == ']') {
						state = ReadDataState.FindProperty;
						if (array) {
							list.add(new com.raven.engine2d.database.GameData(Boolean.parseBoolean(value)));
							state = ReadDataState.FindValue;
						} else {
							data.put(prop.toLowerCase(),
									new com.raven.engine2d.database.GameData(Boolean.parseBoolean(value)));
							state = ReadDataState.FindProperty;
						}
						done = true;
					} else {
						value += chars[i];
					}
					break;
				case Integer:
					if (Character.isWhitespace(chars[i]) || chars[i] == ',') {
						if (array) {
							list.add(new com.raven.engine2d.database.GameData(Integer.parseInt(value)));
							state = ReadDataState.FindValue;
						} else {
							data.put(prop.toLowerCase(),
									new com.raven.engine2d.database.GameData(Integer.parseInt(value)));
							state = ReadDataState.FindProperty;
						}
					} else if (chars[i] == '}' || chars[i] == ']') {
						if (array) {
							list.add(new com.raven.engine2d.database.GameData(Integer.parseInt(value)));
							state = ReadDataState.FindValue;
						} else {
							data.put(prop.toLowerCase(),
									new com.raven.engine2d.database.GameData(Integer.parseInt(value)));
							state = ReadDataState.FindProperty;
						}
						done = true;
					} else {
						value += chars[i];
					}
					break;
				case String:
					if (chars[i] == '"') {
						data.put(prop.toLowerCase(), new com.raven.engine2d.database.GameData(value));
						state = ReadDataState.FindProperty;
						if (array) {
							list.add(new com.raven.engine2d.database.GameData(value));
							state = ReadDataState.FindValue;
						} else {
							data.put(prop.toLowerCase(), new com.raven.engine2d.database.GameData(value));
							state = ReadDataState.FindProperty;
						}
					} else {
						value += chars[i];
					}
					break;
				default:
					break;
				}
				break;

			}

			i++;

			if (i >= chars.length) {
				done = true;
			}
		}

		if (array)
			return new com.raven.engine2d.database.GameData(list);

		if (data.size() != 0)
			return new com.raven.engine2d.database.GameData(data);

		return null;
	}
}
