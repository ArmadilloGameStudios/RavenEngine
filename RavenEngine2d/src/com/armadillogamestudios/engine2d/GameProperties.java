package com.armadillogamestudios.engine2d;

import com.armadillogamestudios.engine2d.util.math.Vector2i;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to access most the general properties of the
 * engine or game
 * Created by cookedbird on 11/15/17.
 */
public class GameProperties {
    private static final int HEIGHT = 360, WIDTH = 640;

    private static float animationSpeed = 1.0f;
    //private static int scaling = 2;
//    private static int height = 1080, width = 1920;
    private static int dheight = 1080, dwidth = 1920;
    private static String mainDirectory;
    private static List<Vector2i> resolutionList = new ArrayList<>();
    private static int sfx = 100, music = 100;
    private static boolean vsync = false;
    private static int windowMode = 0;
    private static boolean useResolution = true;

    public static final int FULLSCREEN = 0, WINDOWED = 1, WINDOWED_BORDERLESS = 2;

    public static int getDisplayWidth() {
        return dwidth;
    }

    public static void setDisplayWidth(int width) {
        GameProperties.dwidth = width;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static int getDisplayHeight() {
        return dheight;
    }

    public static void setDisplayHeight(int height) {
        GameProperties.dheight = height;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static String getMainDirectory() {
        return mainDirectory;
    }

    public static void setMainDirectory(String dir) {
        GameProperties.mainDirectory = dir;
    }

    public static float getAnimationSpeed() {
        return animationSpeed;
    }

    public static void setSFXVolume(int value) {
        sfx = value;
    }

    public static int getSFXVolume() {
        return sfx;
    }

    public static void setMusicVolume(int value) {
        music = value;
    }

    public static int getMusicVolume() {
        return music;
    }

    public static void addResolution(int width, int height) {
        if (resolutionList.stream().noneMatch(r -> r.x == width && r.y == height)) {
            resolutionList.add(new Vector2i(width, height));

            resolutionList.sort((b, a) -> {
                int v = b.x - a.x;
                return v == 0 ? b.y - a.y : v;
            });
        }
    }

    public static List<Vector2i> getResolutionList() {
        return resolutionList;
    }

    public static boolean getUseResolution() {
        return useResolution;
    }

    public static void setUseResolution(boolean useResolution) {
        GameProperties.useResolution = useResolution;
    }

    public static void setVSync(boolean vsync) {
        GameProperties.vsync = vsync;
    }

    public static boolean getVSync() {
        return vsync;
    }

    public static void setWindowMode(int windowMode) {
        GameProperties.windowMode = windowMode;
    }

    public static Integer getWindowMode() {
        return windowMode;
    }

    private GameProperties() {}
}
