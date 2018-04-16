package com.raven.breakingsands;

public enum ZLayer {
    UI(1f), TERRAIN(.7f), PAWN(.4f), DECAL(.45f);

    private float value;
    ZLayer(float value) {
        this.value = value;
    }
    public float getValue() {
        return value;
    }
}
