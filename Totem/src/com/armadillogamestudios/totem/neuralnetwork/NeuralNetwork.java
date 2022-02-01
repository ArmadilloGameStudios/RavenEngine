package com.armadillogamestudios.totem.neuralnetwork;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork {
    private final int inputSize;
    private final int hiddenSize;
    private final int outputSize;

    private final List<Neuron> inputLayer;
    private final List<Neuron> hiddenLayer;
    private final List<Neuron> outputLayer;

    private float learningRate = 0.01f;
    private float momentum = 0.5f;
    private IActivationFunction activationFunction = new LeakyReLu(); // default activation function
    private boolean initialized = false;

    public NeuralNetwork(int inputSize, int hiddenSize, int outputSize) {
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.outputSize = outputSize;
        this.inputLayer = new ArrayList<>();
        this.hiddenLayer = new ArrayList<>();
        this.outputLayer = new ArrayList<>();
    }

    public void setLearningRate(float learningRate) {
        this.learningRate = learningRate;
    }

    public void setMomentum(float momentum) {
        this.momentum = momentum;
    }

    public void setActivationFunction(ActivationFunction activationFunction) {
        switch (activationFunction) {
            case LEAKY_RELU:
                this.activationFunction = new LeakyReLu();
                break;
            case TANH:
                this.activationFunction = new TanH();
                break;
            case SIGMOID:
                this.activationFunction = new Sigmoid();
                break;
            case SWISH:
                this.activationFunction = new Swish();
                break;
        }
    }

    public void init() {
        for (int i = 0; i < inputSize; i++) {
            this.inputLayer.add(new Neuron());
        }
        for (int i = 0; i < hiddenSize; i++) {
            this.hiddenLayer.add(new Neuron(this.inputLayer, activationFunction));
        }
        for (int i = 0; i < outputSize; i++) {
            this.outputLayer.add(new Neuron(this.hiddenLayer, activationFunction));
        }
        this.initialized = true;
        System.out.println("Network Initialized.");
    }


    public void train(MLDataSet set, int epoch) {
        if (!initialized){
            this.init();
        }
        System.out.println("Training Started");
        for (int i = 0; i < epoch; i++) {

            if (i % 100 == 0) {
                System.out.println(" " + (i / epoch * 100) + " " + i + " / " + epoch);
            }

            Collections.shuffle(set.getData());

            for (MLData datum : set.getData()) {
                forward(datum.getInputs());
                backward(datum.getTargets());
            }
        }
        System.out.println("Training Finished.");
    }

    private void backward(float[] targets) {
        int i = 0;
        for (Neuron neuron : outputLayer) {
            neuron.calculateGradient(targets[i++]);
        }
        for (Neuron neuron : hiddenLayer) {
            neuron.calculateGradient();
        }
        for (Neuron neuron : hiddenLayer) {
            neuron.updateConnections(learningRate, momentum);
        }
        for (Neuron neuron : outputLayer) {
            neuron.updateConnections(learningRate, momentum);
        }
    }

    private void forward(float[] inputs) {
        int i = 0;
        for (Neuron neuron : inputLayer) {
            neuron.setOutput(inputs[i++]);
        }
        for (Neuron neuron : hiddenLayer) {
            neuron.calculateOutput();
        }
        for (Neuron neuron : outputLayer) {
            neuron.calculateOutput();
        }
    }

    public double[] predict(float... inputs) {
        forward(inputs);
        double[] output = new double[outputLayer.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = outputLayer.get(i).getOutput();
        }
        System.out.println("Input : " + Arrays.toString(inputs) + " Predicted : " + Arrays.toString(output));
        return output;
    }

}
