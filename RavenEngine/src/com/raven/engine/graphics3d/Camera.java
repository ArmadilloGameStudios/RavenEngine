package com.raven.engine.graphics3d;

import com.raven.engine.GameProperties;
import com.raven.engine.util.Matrix4f;
import com.raven.engine.util.Vector3f;

/**
 * Created by cookedbird on 11/15/17.
 */
public class Camera {
    float x, y, zoom = -30f, zoomMin = -25f, zoomMax = -50f, xr, yr = 40, yrMin = 20, yrMax = 89;
    float xs = x, ys = y, zooms = zoom, xrs = xr, yrs = yr;

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    public Camera() {
        projectionMatrix = Matrix4f.perspective(60.0f, ((float) GameProperties.getScreenWidth())
                / ((float) GameProperties.getScreenHeight()), .2f, 100.0f);

        updateViewMatrix();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void zoom(double yoffset) {
        zoom += yoffset * 3f;
        zoom = Math.min(zoomMin, Math.max(zoomMax, zoom));
    }

    public void rotate(double x, double y) {
        xr += x * .2f;
        yr += y * .2f;
        yr = Math.max(yrMin, Math.min(yrMax, yr));
    }

    public void move(double x, double y) {
        this.x += (x * +Math.cos(xrs / 180.0 * Math.PI) +
                   y * -Math.sin(xrs / 180.0 * Math.PI)) *
                -zoom * .001f;
        this.y += (x * +Math.sin(xrs / 180.0 * Math.PI) +
                   y * +Math.cos(xrs / 180.0 * Math.PI)) *
                -zoom * .001f;
    }

    private void updateViewMatrix() {
        viewMatrix = new Matrix4f();

        viewMatrix = viewMatrix.multiply(Matrix4f.translate(0, 0, zooms));
        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(yrs, 1f, 0f, 0f));

        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(xrs, 0f, 1f, 0f));
        viewMatrix = viewMatrix.multiply(Matrix4f.translate(xs, 0, ys));

    }

    public void update(float deltaTime) {
        // smooth motion
        float deltaCorrection = .004f;
        zooms += (zoom - zooms) * 1f * deltaTime * deltaCorrection;
        xs += (x - xs) * 3f * deltaTime * deltaCorrection;
        ys += (y - ys) * 3f * deltaTime * deltaCorrection;
        xrs += (xr - xrs) * 3f * deltaTime * deltaCorrection;
        yrs += (yr - yrs) * 3f * deltaTime * deltaCorrection;

        updateViewMatrix();
    }

    public float getPitch() {
        return yrs;
    }
}
