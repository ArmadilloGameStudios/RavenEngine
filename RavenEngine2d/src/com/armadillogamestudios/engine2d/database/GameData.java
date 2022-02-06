package com.armadillogamestudios.engine2d.database;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GameData implements GameDatable {

    private enum DataType {
        DATA, LIST, INTEGER, BOOL, STRING,
    }

    private Object value;
    private DataType dataType;

    // Constructors
    public GameData() {
        this.value = new HashMap<String, GameData>();
        dataType = DataType.DATA;
    }

    public GameData(GameData data) {
        dataType = data.dataType;
        switch (data.dataType) {
            case DATA:
                value = new HashMap<>((HashMap<String, GameData>) data.value);
                break;
            case BOOL:
                value = data.asBoolean();
                break;
            case STRING:
                value = data.asString();
                break;
            case LIST:
                value = new GameDataList(data.asList());
                break;
            case INTEGER:
                value = data.asInteger();
                break;
        }
    }

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
            return asMap().containsKey(prop.toLowerCase());
        } else {
            return false;
        }
    }

    public void ifHas(String prop, Consumer<GameData> consumer) {
        if (has(prop)) {
            consumer.accept(getData(prop));
        }
    }

    public void ifHas(String prop, Consumer<GameData> consumer, Runnable elseConsumer) {
        if (has(prop)) {
            consumer.accept(getData(prop));
        } else {
            elseConsumer.run();
//            elseConsumer.accept();
        }
    }

    public void throwExceptionIfInvalid(String prop) {
        if (has(prop)) {
            throw new ExceptionGameDataPropertyInvalid(prop + " was found and is not supported");
        }
    }

    public boolean isData() {
        return dataType == DataType.DATA;
    }

    public Map<String, GameData> asMap() {
        return ((Map<String, GameData>) value);
    }

    public GameData getData(String prop) {
        GameData data = null;

        if (isData())
            data = asMap().get(prop.toLowerCase());

        // if (data == null) {}

        return data;
    }

    public boolean isList() {
        return dataType == DataType.LIST;
    }

    public GameDataList asList() {
        return (GameDataList) value;
    }

    public GameDataList getList(String prop) {
        try {
            return getData(prop).asList();
        } catch (Exception e) {
            System.out.println("Error reading \"" + prop + "\" from " + value);
            throw e;
        }
    }

    public void addList(String prop, GameDataList value) {
        asMap().put(prop, new GameData(value));
    }

    public boolean isString() {
        return dataType == DataType.STRING;
    }

    public String asString() {
        return (String) value;
    }

    public String getString(String prop) {
        try {
            return getData(prop).asString();
        } catch (Exception e) {
            System.out.println("Error reading \"" + prop + "\" from " + value);
            throw e;
        }
    }

    public boolean isBoolean() {
        return dataType == DataType.BOOL;
    }

    public boolean asBoolean() {
        return (boolean) value;
    }

    public boolean getBoolean(String prop) {
        return getData(prop).asBoolean();
    }

    public boolean isInteger() {
        return dataType == DataType.INTEGER;
    }

    public int asInteger() {
        return (int) value;
    }

    public int getInteger(String prop) {
        return getData(prop).asInteger();
    }

    @Override
    public String toString() {
        if (isBoolean()) {
            return String.valueOf(asBoolean());
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