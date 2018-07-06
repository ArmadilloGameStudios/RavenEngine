package com.raven.engine2d;

/**
 * A class to access most the general properties of the
 * engine or game
 * Created by cookedbird on 11/15/17.
 */
public class GameProperties {
    private static int scaling = 2;
    private static int height = 1080, width = 1920;
    private static String mainDirectory;

    public static int getScreenWidth() {
        return width;
    }

    public static void setScreenWidth(int width) {
        GameProperties.width = width;
    }

    public static int getScreenHeight() {
        return height;
    }

    public static void setScreenHeight(int height) {
        GameProperties.height = height;
    }

    public static int getScaling() { return scaling; }

    public static void setScaling(int scaling) {
        GameProperties.scaling = scaling;
    }

    public static String getMainDirectory() {
        return mainDirectory;
    }

    public static void setMainDirectory(String dir) {
        GameProperties.mainDirectory = dir;
    }

    private GameProperties() {}
}
