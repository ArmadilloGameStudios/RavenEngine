package com.armadillogamestudios.totem.neuralnetwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MLDataSet {

    private List<MLData> data;

    public MLDataSet() {
        data = new ArrayList<>();
    }

    public MLDataSet(List<InputTargetPair> itpl) {
        this.data = itpl.stream()
                .map(p -> new MLData(p.getInput(), p.getTarget()))
                .collect(Collectors.toList());

        // this.data.forEach(d -> System.out.println(Arrays.toString(d.getInputs())));
        System.out.println(this.data.size());
    }

    public void addMLData(MLData mlData) {
        this.data.add(mlData);
    }

    public List<MLData> getData() {
        return data;
    }

    public void setData(List<MLData> data) {
        this.data = data;
    }
}
