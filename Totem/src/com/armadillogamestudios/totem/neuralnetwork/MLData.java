package com.armadillogamestudios.totem.neuralnetwork;

public class MLData {

    private float[] inputs;
    private float[] targets;

    public MLData(float[] inputs, float[] targets) {
        this.inputs = inputs;
        this.targets = targets;
    }

    public float[] getInputs() {
        return inputs;
    }

    public void setInputs(float[] inputs) {
        this.inputs = inputs;
    }

    public float[] getTargets() {
        return targets;
    }

    public void setTargets(float[] targets) {
        this.targets = targets;
    }
}
