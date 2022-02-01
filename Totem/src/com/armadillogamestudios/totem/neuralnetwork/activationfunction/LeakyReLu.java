package com.armadillogamestudios.totem.neuralnetwork.activationfunction;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.IActivationFunction;

public class LeakyReLu implements IActivationFunction {
    @Override
    public float output(float x) {
        return x >= 0 ? x : (x * 0.01f);
    }

    @Override
    public float outputDerivative(float x) {
        return x >= 0 ? 1 : 0.01f;
    }
}
