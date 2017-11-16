package com.raven.engine.input;

import org.lwjgl.glfw.GLFW;

/**
 * Created by cookedbird on 11/16/17.
 */
public class Mouse {
    double x, y;
    boolean rightButtonDown, middleButtonDown, leftButtonDown;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void buttonAction(int button, int action) {
        switch (button) {
            case (GLFW.GLFW_MOUSE_BUTTON_LEFT):
                if (action == GLFW.GLFW_PRESS) {
                    leftButtonDown = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    leftButtonDown = false;
                }
                break;
            case (GLFW.GLFW_MOUSE_BUTTON_MIDDLE):
                if (action == GLFW.GLFW_PRESS) {
                    middleButtonDown = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    middleButtonDown = false;
                }
                break;
            case (GLFW.GLFW_MOUSE_BUTTON_RIGHT):
                if (action == GLFW.GLFW_PRESS) {
                    rightButtonDown = true;
                } else if (action == GLFW.GLFW_RELEASE) {
                    rightButtonDown = false;
                }
                break;
            default:
        }
    }

    public boolean isRightButtonDown() {
        return rightButtonDown;
    }

    public boolean isMiddleButtonDown() {
        return middleButtonDown;
    }
}
