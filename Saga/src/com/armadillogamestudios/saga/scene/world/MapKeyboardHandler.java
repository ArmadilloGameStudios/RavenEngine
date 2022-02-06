package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.input.KeyData;
import com.armadillogamestudios.engine2d.input.KeyboardHandler;

public class MapKeyboardHandler implements KeyboardHandler {

    private final WorldScene map;

    public MapKeyboardHandler(WorldScene map) {
        this.map = map;
    }

    @Override
    public void onKeyPress(KeyData keyData) {
        switch (keyData.getKey()) {
            case A -> {
                map.moveLeft = true;
            }
            case B -> {
            }
            case C -> {
            }
            case D -> {
                map.moveRight = true;
            }
            case E -> {
            }
            case F -> {
            }
            case G -> {
            }
            case H -> {
            }
            case I -> {
            }
            case J -> {
            }
            case K -> {
            }
            case L -> {
            }
            case M -> {
            }
            case N -> {
            }
            case O -> {
            }
            case P -> {
            }
            case Q -> {
            }
            case R -> {
            }
            case S -> {
                map.moveDown = true;
            }
            case T -> {
            }
            case U -> {
            }
            case V -> {
            }
            case W -> {
                map.moveUp = true;
            }
            case X -> {
            }
            case Y -> {
            }
            case Z -> {
            }
            case SPACE -> {
            }
            case BACKSPACE -> {
            }
            case ESCAPE -> {
            }
            case UNKNOWN -> {
            }
        }
    }

    @Override
    public void onKeyRelease(KeyData keyData) {
        switch (keyData.getKey()) {

            case A -> {
                map.moveLeft = false;
            }
            case B -> {
            }
            case C -> {
            }
            case D -> {
                map.moveRight = false;
            }
            case E -> {
            }
            case F -> {
            }
            case G -> {
            }
            case H -> {
            }
            case I -> {
            }
            case J -> {
            }
            case K -> {
            }
            case L -> {
            }
            case M -> {
            }
            case N -> {
            }
            case O -> {
            }
            case P -> {
            }
            case Q -> {
            }
            case R -> {
            }
            case S -> {
                map.moveDown = false;
            }
            case T -> {
            }
            case U -> {
            }
            case V -> {
            }
            case W -> {
                map.moveUp = false;
            }
            case X -> {
            }
            case Y -> {
            }
            case Z -> {
            }
            case SPACE -> {
            }
            case BACKSPACE -> {
            }
            case ESCAPE -> {
            }
            case UNKNOWN -> {
            }
        }
    }
}
