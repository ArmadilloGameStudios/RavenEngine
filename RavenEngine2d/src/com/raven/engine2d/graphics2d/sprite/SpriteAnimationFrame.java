package com.raven.engine2d.graphics2d.sprite;

import com.raven.engine2d.database.GameData;

public class SpriteAnimationFrame {
    private int x, y, width, height, time, index;

    public SpriteAnimationFrame(GameData gdAction, GameData gdFrame, int index) {
        time = gdFrame.getInteger("time");
        this.index = index;

        if (gdFrame.has("x")) {
            x = gdFrame.getInteger("x");
        } else {
            x = 0;
        }

        if (gdFrame.has("y")) {
            y = gdFrame.getInteger("y");
        } else {
            y = 0;
        }

        if (gdFrame.has("width")) {
            width = gdFrame.getInteger("width");
        } else {
            width = gdAction.getInteger("width");
        }

        if (gdFrame.has("height")) {
            height = gdFrame.getInteger("height");
        } else {
            height = gdAction.getInteger("height");
        }
    }

    public int getTime() {
        return time;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIndex() {
        return index;
    }
}
