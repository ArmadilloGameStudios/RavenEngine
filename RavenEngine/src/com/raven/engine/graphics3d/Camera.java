package com.raven.engine.graphics3d;

import com.raven.engine.GameProperties;
import com.raven.engine.util.Matrix4f;

/**
 * Created by cookedbird on 11/15/17.
 */
public class Camera {
    float x, y, zoom = -30f, zoomMin = -25f, zoomMax = -50f, xr, yr = 40, yrMin = 20, yrMax = 89;

    private Matrix4f viewMatrix;
    private Matrix4f projectionMatrix;

    public Camera() {
        projectionMatrix = Matrix4f.perspective(60.0f, ((float) GameProperties.getScreenWidth())
                / ((float) GameProperties.getScreenHeight()), 2f, 100.0f);

        updateViewMatrix();
    }

    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void zoom(double yoffset) {zoom += yoffset * 2f;
        zoom = Math.min(zoomMin, Math.max(zoomMax, zoom));

        updateViewMatrix();
    }

    public void rotate(double x, double y) {
        xr += x;
        yr += y * .2f;
        yr = Math.max(yrMin, Math.min(yrMax, yr));

        updateViewMatrix();
    }

    public void move(double x, double y) {
        this.x += x * .1f;
        this.y += y * .1f;

        updateViewMatrix();
    }

    private void updateViewMatrix() {
        viewMatrix = Matrix4f.translate(0, 0, zoom);
        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(yr, 1f, 0f, 0f));
        viewMatrix = viewMatrix.multiply(Matrix4f.translate(x, 0, y));
        viewMatrix = viewMatrix.multiply(Matrix4f.rotate(xr, 0f, 1f, 0f));

        // viewMatrix = viewMatrix.multiply(Matrix4f.rotate((float)x * .2f, 0f, 1f, 0f).multiply(Matrix4f.rotate((float)y * -.2f, 1f, 0f, 0f)));
    }

}
