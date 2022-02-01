package com.armadillogamestudios.totem.neuralnetwork;

public class InputTargetPair {

    private float[] input, target;

    public InputTargetPair(float[][] data) {
        input = data[0];
        target = data[1];
    }

    public InputTargetPair(float[] input, float[] target) {
        this.input = input;
        this.target = target;
    }

    public float[] getInput() {
        return input;
    }

    public float[] getTarget() {
        return target;
    }
}
