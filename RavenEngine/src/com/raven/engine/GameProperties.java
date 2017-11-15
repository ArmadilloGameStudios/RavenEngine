package com.raven.engine;

/**
 * A class to access most the general properties of the
 * engine or game
 * Created by cookedbird on 11/15/17.
 */
public class GameProperties {
    public static int getScreenWidth() {
        return GameEngine.getEngine().getGame().getWidth();
    }

    public static int getScreenHeight() {
        return GameEngine.getEngine().getGame().getHeight();
    }

    private GameProperties() {}
}
