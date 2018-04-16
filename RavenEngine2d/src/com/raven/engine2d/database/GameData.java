package com.raven.engine2d.database;

import java.util.Map;

public class GameData implements GameDatable {

    private enum DataType {
        DATA, LIST, INTEGER, BOOL, STRING,
    }

    private Object value;
    private DataType dataType;

    // Constructors
    public GameData(Map<String, GameData> value) {
        this.value = value;
        dataType = DataType.DATA;
    }

    public GameData(GameDataList value) {
        this.value = value;
        dataType = DataType.LIST;
    }

    public GameData(boolean value) {
        this.value = value;
        dataType = DataType.BOOL;
    }

    public GameData(String value) {
        this.value = value;
        dataType = DataType.STRING;
    }

    public GameData(int value) {
        this.value = value;
        dataType = DataType.INTEGER;
    }

    public boolean has(String prop) {
        if (isData()) {
            return asData().containsKey(prop);
        } else {
            return false;
        }
    }

    public boolean isData() {
        return dataType == DataType.DATA;
    }

    private Map<String, GameData> asData() {
        return ((Map<String, GameData>) value);
    }

    public GameData asData(String prop) {
        if (isData())
            return asData().get(prop.toLowerCase());
        return null;
    }

    public boolean isList() {
        return dataType == DataType.LIST;
    }

    public GameDataList asList() {
        return (GameDataList) value;
    }

    public GameDataList getList(String prop) {
        return asData(prop).asList();
    }

    public void addList(String prop, GameDataList value) {
        asData().put(prop, new GameData(value));
    }

    public boolean isString() {
        return dataType == DataType.STRING;
    }

    public String asString() {
        return (String) value;
    }

    public String getString(String prop) {
        return asData(prop).asString();
    }

    public boolean isBoolean() {
        return dataType == DataType.BOOL;
    }

    public boolean asBoolean() {
        return (boolean) value;
    }

    public boolean getBoolean(String prop) {
        return asData(prop).asBoolean();
    }

    public boolean isInteger() {
        return dataType == DataType.INTEGER;
    }

    public int asInteger() {
        return (int) value;
    }

    public int getInteger(String prop) {
        return asData(prop).asInteger();
    }

    @Override
    public String toString() {
        if (isBoolean()) {
            return String.valueOf(this.asBoolean());
        } else if (isInteger()) {
            return String.valueOf(asInteger());
        } else if (isString()) {
            return String.format("\"%1$s\"", asString());
        } else if (this.isData()) {
            return String.valueOf(value);
        } else if (this.isList()) {
            return String.valueOf(value);
        }

        return super.toString();
    }

    @Override
    public GameData toGameData() {
        return this;
    }
}
