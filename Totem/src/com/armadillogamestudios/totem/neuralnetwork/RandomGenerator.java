package com.armadillogamestudios.totem.neuralnetwork;

import java.util.Random;

public class RandomGenerator {

    private int seed = 1; // 474

    public static Random random = new Random();

    public static float randomValue(float min, float max) {
        return min + (max - min) * random.nextFloat();
    }

    public static int random(int max) {
        return random.nextInt(max);
    }

}
