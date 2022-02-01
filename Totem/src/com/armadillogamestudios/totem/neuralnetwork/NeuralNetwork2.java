package com.armadillogamestudios.totem.neuralnetwork;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork2 {

    private final int[] layerSizes;
    private final List<List<Neuron>> layers;
    private List<Neuron> inputLayer, outputLayer;

    private float learningRate = 0.01f;
    private float momentum = 0.5f;
    private IActivationFunction activationFunction = new LeakyReLu(); // default activation function
    private boolean initialized = false;

    public NeuralNetwork2(int... layerSizes) {

        System.out.println(Arrays.toString(layerSizes));

        this.layerSizes = layerSizes;
        this.layers = new ArrayList<>();
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

        for (int layerIndex = 0; layerIndex < layerSizes.length; layerIndex++) {
            List<Neuron> layer = new ArrayList<>();
            layers.add(layer);

            if (layerIndex == 0)
                inputLayer = layer;
            else if (layerIndex == layerSizes.length - 1)
                outputLayer = layer;

            for (int neuronIndex = 0; neuronIndex < layerSizes[layerIndex]; neuronIndex++) {
                if (layerIndex == 0) {
                    layer.add(new Neuron());
                } else {
                    layer.add(new Neuron(layers.get(layerIndex - 1), activationFunction));
                }
            }
        }

        this.initialized = true;
        System.out.println("Network Initialized.");
    }

    public void train(MLDataSet set, int epoch) {
        if (!initialized) {
            this.init();
        }
        System.out.println("Training Started");

        long startTime = System.nanoTime();

        for (int i = 0; i < epoch; i++) {

            if (i % 100 == 0 && i != 0) {
                long diffTime = System.nanoTime() - startTime;
                double seconds = diffTime / 1000000000.0;
                double rate = (double) i / seconds;
                double remain = (epoch - i) / rate;
                int rSeconds = (int) remain;
                int rSec = rSeconds % 60;
                int rMin = rSeconds / 60;

                System.out.println(" " + (i * 100 / epoch) + "% " + i + "/" + epoch + " " + rMin + ":" + rSec);
            }

            List<MLData> data = new ArrayList<>();
            data.addAll(Transformation.randomRotation(set.getData(), .2f, 0f));
            data.addAll(Transformation.random(10, set.getData().get(0).getTargets().length));
            Collections.shuffle(data, RandomGenerator.random);

            for (MLData datum : data) {
                forward(datum.getInputs());
                backward(datum.getTargets());
            }

            if (Float.isNaN(outputLayer.get(0).getGradient())) {
                throw new NullPointerException();
            }
        }
        System.out.println("Training Finished.");
    }

    private void backward(float[] targets) {

        int i = 0;
        for (Neuron neuron : outputLayer) {
            neuron.calculateGradient(targets[i++]);
        }

        for (i = 1; i < layers.size() - 1; i++) {
            for (Neuron neuron : layers.get(layers.size() - 1 - i)) {
                neuron.calculateGradient();
            }
        }

        update();
    }

    private void update() {

        for (int i = 0; i < layers.size(); i++) {
            for (Neuron neuron : layers.get(i)) {
                neuron.updateConnections(learningRate, momentum);
            }
        }
    }

    private void forward(float[] inputs) {
        int i = 0;
        for (Neuron neuron : inputLayer) {
            neuron.setOutput(inputs[i++]);
        }

        for (i = 1; i < layerSizes.length; i++) {
            for (Neuron neuron : layers.get(i)) {
                neuron.calculateOutput();
            }
        }
    }

    public float[] predict(float... inputs) {

        inputs = Transformation.normalizeAndAddAngles(inputs);

        forward(inputs);

        float[] output = new float[outputLayer.size()];
        for (int i = 0; i < output.length; i++) {
            output[i] = outputLayer.get(i).getOutput();
        }

        return output;
    }

    public void save(String filename) throws IOException {
        DataOutputStream os = new DataOutputStream(new FileOutputStream(Path.of(filename).toFile()));

        // save layer info
        os.writeInt(layerSizes.length);

        for (int i = 0; i < layerSizes.length; i++) {
            // Layer
            os.writeInt(layerSizes[i]);
        }

        for (int i = 0; i < layerSizes.length; i++) {
            for (int j = 0; j < layerSizes[i]; j++) {
                // Neurons
                os.writeFloat(layers.get(i).get(j).getBias());

                for (int k = 0; k < layers.get(i).get(j).getOutgoingConnections().size(); k++) {
                    // Connections

                    Connection connection = layers.get(i).get(j).getOutgoingConnections().get(k);

                    os.writeFloat(connection.getSynapticWeight());
                }
            }
        }

        os.flush();
        os.close();
    }

    public static NeuralNetwork2 load(String filename) throws IOException {

        DataInputStream os = new DataInputStream(new FileInputStream(Path.of(filename).toFile()));

        int layerCount = os.readInt();
        int[] layerSizes = new int[layerCount];

        for (int i = 0; i < layerSizes.length; i++) {
            // Layer
            layerSizes[i] = os.readInt();
        }

        NeuralNetwork2 neuralNetwork = new NeuralNetwork2(layerSizes);
        neuralNetwork.init();

        for (int i = 0; i < layerSizes.length; i++) {
            for (int j = 0; j < layerSizes[i]; j++) {
                // Neurons
                Neuron neuron = neuralNetwork.layers.get(i).get(j);
                neuron.setBias(os.readFloat());

                for (int k = 0; k < neuralNetwork.layers.get(i).get(j).getOutgoingConnections().size(); k++) {
                    // Connections

                    Connection connection = neuralNetwork.layers.get(i).get(j).getOutgoingConnections().get(k);
                    connection.setSynapticWeight(os.readFloat());
                }
            }
        }

        os.close();

        return neuralNetwork;
    }
}
