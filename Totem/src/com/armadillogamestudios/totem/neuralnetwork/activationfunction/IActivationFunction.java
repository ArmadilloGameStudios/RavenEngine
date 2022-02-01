package com.armadillogamestudios.totem.neuralnetwork.activationfunction;

public interface IActivationFunction {
    float output(float x);

    float outputDerivative(float x);
}
