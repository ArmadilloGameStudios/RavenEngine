package com.armadillogamestudios.totem.neuralnetwork;

import com.armadillogamestudios.totem.neuralnetwork.activationfunction.IActivationFunction;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Neuron {

    private UUID neuronId;
    private List<Connection> incomingConnections;
    private List<Connection> outgoingConnections;
    private float bias;
    private float gradient;
    private float output;
    private float outputBeforeActivation;
    private IActivationFunction activationFunction;

    public Neuron() {
        this.neuronId = UUID.randomUUID();
        this.incomingConnections = new ArrayList<>();
        this.outgoingConnections = new ArrayList<>();
        this.bias = RandomGenerator.randomValue(.0f, .25f);
    }

    public Neuron(List<Neuron> neurons, IActivationFunction activationFunction) {
        this();
        this.activationFunction = activationFunction;
        for (Neuron neuron : neurons) {
            Connection connection = new Connection(neuron, this);
            neuron.getOutgoingConnections().add(connection);
            this.incomingConnections.add(connection);
        }
    }

    public void calculateOutput() {
        this.outputBeforeActivation = 0.0f;
        for (Connection connection : incomingConnections) {
            this.outputBeforeActivation += connection.getSynapticWeight() * connection.getFrom().getOutput();
        }
        this.output = activationFunction.output(this.outputBeforeActivation + bias);
    }

    public float error(float target) {
        return target - output;
    }

    public void calculateGradient(float target) {
        this.gradient = error(target) * activationFunction.outputDerivative(output);
    }

    public void calculateGradient() {
        this.gradient = (float) (outgoingConnections.stream()
                        .mapToDouble(connection -> connection.getTo().getGradient() * connection.getSynapticWeight())
                        .sum() * activationFunction.outputDerivative(output));
    }

    public void updateConnections(float lr, float mu) {

        for (Connection connection : incomingConnections) {
            double prevDelta = connection.getSynapticWeightDelta();
            connection.setSynapticWeightDelta(lr * gradient * connection.getFrom().getOutput());
            connection.updateSynapticWeight(connection.getSynapticWeightDelta() + mu * prevDelta);
        }
    }

    public List<Connection> getOutgoingConnections() {
        return outgoingConnections;
    }

    public float getOutput() {
        return output;
    }

    public float getGradient() {
        return gradient;
    }

    public void setOutput(float output) {
        this.output = output;
    }

    public float getBias() {
        return bias;
    }

    public void setBias(float bias) {
        this.bias = bias;
    }
}
