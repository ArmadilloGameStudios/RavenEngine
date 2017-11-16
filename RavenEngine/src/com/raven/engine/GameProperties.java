package com.raven.engine;

/**
 * A class to access most the general properties of the
 * engine or game
 * Created by cookedbird on 11/15/17.
 */
public class GameProperties {
    public static int getScreenWidth() {
        return 1920;
    }

    public static int getScreenHeight() {
        return 1080;
    }

    public static String getMainDirectory() {
        return GameEngine.getEngine().getGame().getMainDirectory();
    }

    private GameProperties() {}
}
