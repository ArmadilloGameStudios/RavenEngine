package com.armadillogamestudios.engine2d.input;

public interface MouseClickHandler extends MouseHandler {
    default void handleMouseEnter() {}
    default void handleMouseLeave() {}
    default void handleMouseHover(float delta) {}
}
