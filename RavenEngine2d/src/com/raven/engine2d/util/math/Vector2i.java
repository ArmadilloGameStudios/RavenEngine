package com.raven.engine2d.util.math;

public class Vector2i {
    public int x, y;

    public Vector2i add(Vector2i other, Vector2i out) {
        out.x = this.x + other.x;
        out.y = this.y + other.y;

        return out;
    }
}