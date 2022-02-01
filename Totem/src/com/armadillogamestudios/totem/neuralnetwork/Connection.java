package com.armadillogamestudios.totem.neuralnetwork;

import java.util.UUID;

public class Connection {

    private UUID connectionId;
    private Neuron from;
    private Neuron to;
    private float synapticWeight;
    private float synapticWeightDelta;

    public Connection(Neuron from, Neuron to) {
        this.connectionId = UUID.randomUUID();
        this.from = from;
        this.to = to;
        this.synapticWeight = RandomGenerator.randomValue(-.5f, .5f);
    }

    public void updateSynapticWeight(double synapticWeight) {
        this.synapticWeight += synapticWeight;
    }

    public float getSynapticWeight() {
        return synapticWeight;
    }

    public Neuron getFrom() {
        return from;
    }

    public Neuron getTo() {
        return to;
    }

    public float getSynapticWeightDelta() {
        return synapticWeightDelta;
    }

    public void setSynapticWeightDelta(float synapticWeightDelta) {
        this.synapticWeightDelta = synapticWeightDelta;
    }

    public void setSynapticWeight(float synapticWeight) {
        this.synapticWeight = synapticWeight;
    }
}
