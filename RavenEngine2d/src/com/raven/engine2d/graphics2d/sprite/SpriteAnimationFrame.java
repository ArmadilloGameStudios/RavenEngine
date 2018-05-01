package com.raven.engine2d.graphics2d.sprite;

import com.raven.engine2d.database.GameData;

import java.sql.SQLSyntaxErrorException;

public class SpriteAnimationFrame {
    private int x, y, x_offset, y_offset, width, height, time, index;

    public SpriteAnimationFrame(GameData gdAction, GameData gdFrame, int index) {
        time = gdFrame.getInteger("time");
        this.index = index;

        if (gdFrame.has("x")) {
            x = gdFrame.getInteger("x");
        } else if (gdAction.has("x")) {
            x = gdAction.getInteger("x");
        } else {
            x = 0;
        }

        if (gdFrame.has("y")) {
            y = gdFrame.getInteger("y");
        } else if (gdAction.has("y")) {
            y = gdAction.getInteger("y");
        } else {
            y = 0;
        }

        if (gdFrame.has("xoffset")) {
            x_offset = gdFrame.getInteger("xoffset");
        } else if (gdAction.has("xoffset")) {
            x_offset = gdAction.getInteger("xoffset");
        } else {
            x_offset = 0;
        }

        if (gdFrame.has("yoffset")) {
            y_offset = gdFrame.getInteger("yoffset");
        } else if (gdAction.has("yoffset")) {
            y_offset = gdAction.getInteger("yoffset");
        } else {
            y_offset = 0;
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

    public int getXOffset() {
        return x_offset;
    }

    public int getYOffset() {
        return y_offset;
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
