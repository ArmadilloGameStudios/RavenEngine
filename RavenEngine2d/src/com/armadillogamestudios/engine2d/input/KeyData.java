package com.armadillogamestudios.engine2d.input;

import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Set;

public class KeyData {

    private Key key;
    private Set<Mod> mods = new HashSet<>();

    public void update(int key, int mods) {
        this.mods.clear();

        this.key = getKeyFromInt(key);
    }

    private Key getKeyFromInt(int code) {
        switch (code) {
            case GLFW.GLFW_KEY_A:
                return Key.A;
            case GLFW.GLFW_KEY_B:
                return Key.B;
            case GLFW.GLFW_KEY_C:
                return Key.C;
            case GLFW.GLFW_KEY_D:
                return Key.D;
            case GLFW.GLFW_KEY_E:
                return Key.E;
            case GLFW.GLFW_KEY_F:
                return Key.F;
            case GLFW.GLFW_KEY_G:
                return Key.G;
            case GLFW.GLFW_KEY_H:
                return Key.H;
            case GLFW.GLFW_KEY_I:
                return Key.I;
            case GLFW.GLFW_KEY_J:
                return Key.J;
            case GLFW.GLFW_KEY_K:
                return Key.K;
            case GLFW.GLFW_KEY_L:
                return Key.L;
            case GLFW.GLFW_KEY_M:
                return Key.M;
            case GLFW.GLFW_KEY_N:
                return Key.N;
            case GLFW.GLFW_KEY_O:
                return Key.O;
            case GLFW.GLFW_KEY_P:
                return Key.P;
            case GLFW.GLFW_KEY_Q:
                return Key.Q;
            case GLFW.GLFW_KEY_R:
                return Key.R;
            case GLFW.GLFW_KEY_S:
                return Key.S;
            case GLFW.GLFW_KEY_T:
                return Key.T;
            case GLFW.GLFW_KEY_U:
                return Key.U;
            case GLFW.GLFW_KEY_V:
                return Key.V;
            case GLFW.GLFW_KEY_W:
                return Key.W;
            case GLFW.GLFW_KEY_Y:
                return Key.Y;
            case GLFW.GLFW_KEY_X:
                return Key.X;
            case GLFW.GLFW_KEY_Z:
                return Key.Z;

            case GLFW.GLFW_KEY_ESCAPE:
                return Key.ESCAPE;
            case GLFW.GLFW_KEY_BACKSPACE:
                return Key.BACKSPACE;
            case GLFW.GLFW_KEY_SPACE:
                return Key.SPACE;
            default:
                return Key.UNKNOWN;
        }
    }

    public Key getKey() {
        return key;
    }

    public enum Key {
        A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z,
        SPACE {
            public String toString() {
                return " ";
            }
        },
        BACKSPACE {
            public String toString() {
                return "";
            }
        },
        ESCAPE {
            public String toString() {
                return "";
            }
        },
        UNKNOWN {
            public String toString() {
                return "";
            }
        },
    }

    public enum Mod {

    }
}
