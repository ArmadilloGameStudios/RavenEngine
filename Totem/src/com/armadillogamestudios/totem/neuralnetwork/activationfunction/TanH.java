package com.armadillogamestudios.totem.neuralnetwork.activationfunction;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.IActivationFunction;

public class TanH implements IActivationFunction {

    @Override
    public float output(float x) {
        return (float) Math.tanh(x);
    }

    @Override
    public float outputDerivative(float x) {
        return (float) (1 - Math.pow(Math.tanh(x), 2));
    }

}
