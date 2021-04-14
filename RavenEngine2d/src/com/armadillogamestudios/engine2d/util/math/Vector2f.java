package com.armadillogamestudios.engine2d.util.math;

public class Vector2f {
    public float x, y;

    public Vector2f() {

    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f other, Vector2f out) {
        out.x = this.x + other.x;
        out.y = this.y + other.y;

        return out;
    }

    public Vector2f add(float x, float y, Vector2f out) {
        out.x = this.x + x;
        out.y = this.y + y;

        return out;
    }

    public Vector2f subtract(Vector2f other, Vector2f out) {
        out.x = this.x - other.x;
        out.y = this.y - other.y;

        return out;
    }

    public Vector2f scale(float s, Vector2f out) {
        out.x = this.x * s;
        out.y = this.y * s;

        return out;
    }

    public Vector2f divide(float scalar, Vector2f out) {
        return scale(1f / scalar, out);
    }

    public Vector2f normalize(Vector2f out) {
        float length = length();
        return divide(length, out);
    }

    public float length() {
        return (float) Math.sqrt(length2());
    }

    public float length2() {
        return x*x + y*y;
    }

    @Override
    public String toString() {
        return "" + x + " " + y;
    }

}