package com.raven.engine2d;

/**
 * A class to access most the general properties of the
 * engine or game
 * Created by cookedbird on 11/15/17.
 */
public class GameProperties {
    private static boolean supportsOpenGL4 = true;
    private static int scaling = 2;

    public static int getScreenWidth() {
        return 1920;
    }

    public static int getScreenHeight() {
        return 1080;
    }

    public static int getScaling() { return scaling; }

    public static void setScaling(int scaling) {
        GameProperties.scaling = scaling;
    }

    public static String getMainDirectory() {
        return GameEngine.getEngine().getGame().getMainDirectory();
    }

    public static void setSupportsOpenGL4(boolean supportsOpenGL4) {
        GameProperties.supportsOpenGL4 = supportsOpenGL4;
    }

    public static boolean supportsOpenGL4() {
        return supportsOpenGL4;
    }

    private GameProperties() {}
}
