package com.armadillogamestudios.totem.neuralnetwork.activationfunction;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.IActivationFunction;

public class Sigmoid implements IActivationFunction {

    @Override
    public float output(float x) {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    @Override
    public float outputDerivative(float x) {
        return x * (1 - x);
    }

}
