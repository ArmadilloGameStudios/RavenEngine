package com.armadillogamestudios.totem.neuralnetwork;

import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.totem.SpellFunction;
import com.armadillogamestudios.totem.neuralnetwork.activationfunction.ActivationFunction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final int spell_size = 64;

//    public static final float[][] SPELL_DATA = {
//            {
//                    1, 1, 0, 1, 1, 0, -1, -1
//            },
//            {
//                    0, 1, 0, 1, 1, 0, 1, 0,
//            },
//            {
//                    -1, -1, 1, 0, -1, 1, 0, 1
//            },
//            {
//                    -1, 0, 1, 1, 0, -1, -1, 1
//            },
//            {
//                    -1, 0, 1, 1, 0, 1, 0, 1,
//            },
//            {
//                    -1, -1, 1, 0, -1, -1, 1, 0,
//            },
//            {
//                    0, 1, -1, 0, 0, 1, 1, 0
//            },
//            {
//                    -2, 3, 0, 1, -2, 3, -3, 2
//            },
//            {
//                    -1, 1, 0, 1, 2, 3, 2, 3,
//            },
//            {
//                    -2, 3, 1, 3, -1, 3, 1, 3,
//            },
//            {
//                    -3, 4, -1, 1, 0, 1, 3, -1,
//            },
//            {
//                    -1, 0, -1, 1, 0, 1, 1, 1
//            },
//            {
//                    -1, 1, -2, 3, -4, 5, -2, 3,
//            },
//            {
//                    1, 1, -2, 3, -4, -5, -2, 3,
//            },
//            {
//                    0, 1, 1, 2, 0, -1, -1, -2,
//            },
//    };

    public static SpellFunction[] SPELL_FUNCTIONS = {
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = f - 1.5f;
                vec.y = -f + .5f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = f - 1.5f * (f + .1f);
                vec.y = (float) Math.sin(-f * 3f + .5f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = (float) Math.tan(f * .2f + .2f);
                vec.y = (float) Math.tan(-f * 2f + .5f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = f - 1f;
                vec.y = 1.2f - (f * f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = f - 2f + (float) Math.sin((f * (f - 1f) * 2f)) * 1.6f;
                vec.y = 1.2f - (f * f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = f * (float) Math.signum(Math.sin(f * 2f)) + .01f;
                vec.y = f * (float) Math.signum(Math.cos(f * 1.9f)) + .01f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                double f = t * 0.1;

                vec.x = (float) (Math.pow(f, 1.5) * Math.signum(Math.sin(f * 2.0)) + .01);
                vec.y = (float) (Math.pow(f  - 1.0, 2.0) * Math.signum(Math.sin(f * 1.3)) + .01);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                double f = t * 0.1;

                vec.x = (float) (f * Math.signum(Math.sin(f * 2.0)) + .01);
                vec.y = (float) (f * f * Math.signum(Math.sin(f * 1.3)) + .01);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                double f = t * 0.1;

                vec.x = (float) (f * Math.signum(Math.sin(f * 2.0)) + .01);
                vec.y = (float) (Math.sin(f * 1.3) + .01);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                double f = t * 0.1;

                vec.x = (float) (f * Math.signum(Math.tan(f * 1.6)) + .01);
                vec.y = (float) (Math.tan(f * 1.3) + .01);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = (float) Math.cos(f * 4f) + .21f;
                vec.y = (float) Math.sin(f * 4f) - .35f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = (float) Math.cos(f * 4f) + .41f - f;
                vec.y = (float) Math.sin(f * 5f) - .35f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f;

                vec.x = (float) Math.signum(Math.cos(f * 2.1f)) + .41f - f;
                vec.y = (float) Math.signum(Math.tan(f * 2f)) - .35f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f - 1f;

                vec.x = (float) ((Math.signum(Math.sin(f * 9f)) + 1f) / 2f);;
                vec.y = (float) ((Math.signum(Math.sin(f * 9f)) - 1f) / 2f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f - 1f;

                vec.x = (float) (Math.pow(Math.sin(f * 3f), 2) * 6f - .2f);
                vec.y = (float) (Math.pow(2, f) - 3.5f);

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f - 1f;

                vec.x = (float) (Math.pow(Math.sin(f * 4f + 2f), 2) - .2f);
                vec.y = (float) Math.cos(f * 4f + 2.1f) * .12f + (f - 1f) * .2f;

                return vec;
            },
            t -> {
                Vector2f vec = new Vector2f();

                float f = t * 0.1f - 1f;

                vec.x = (float) (Math.pow(Math.sin(f * 6f + 2f), 3) - .4f);
                vec.y = (float) Math.cos(f * 4f + 2.1f) * .42f + (f * f - 1f) * .5f;

                return vec;
            }
    };

    public static float[][] SPELL_DATA;

    static {
        SPELL_DATA = new float[SPELL_FUNCTIONS.length][];

        for (int i = 0; i < SPELL_DATA.length; i++) {
            SPELL_DATA[i] = new float[spell_size];

            for (int t = 0; t < spell_size / 2; t++) {
                Vector2f vec = SPELL_FUNCTIONS[i].compute(t);

                System.out.println(t + "= " + vec.toString());

                SPELL_DATA[i][t * 2] = vec.x;
                SPELL_DATA[i][t * 2 + 1] = vec.y;
            }
        }
    }

    public static void main(String[] args) {

        NeuralNetwork2 neuralNetwork = null;

        if (!Files.exists(Path.of("Totem\\nn.net"))) {
            neuralNetwork = new NeuralNetwork2(
                    spell_size,
                    SPELL_DATA.length * 2 * 3,
                    SPELL_DATA.length * 3,
                    SPELL_DATA.length * 2,
                    SPELL_DATA.length);

            neuralNetwork.init();
            neuralNetwork.setLearningRate(0.01f);
            neuralNetwork.setMomentum(0.2f);
            neuralNetwork.setActivationFunction(ActivationFunction.SWISH);

            List<InputTargetPair> inputTargetPairList = Transformation.spell(SPELL_DATA);

            MLDataSet dataSet = new MLDataSet(inputTargetPairList);
            neuralNetwork.train(dataSet, 40000);

            for (float[] spell : SPELL_DATA) {
                float[] output = neuralNetwork.predict(spell);

                System.out.println(Arrays.toString(output));
                System.out.println(highestIndex(output));
            }

            // TODO check accuracy

            try {
                neuralNetwork.save("Totem\\nn.net");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                neuralNetwork = NeuralNetwork2.load("Totem\\nn.net");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (float[] spell : SPELL_DATA) {
            float[] output = neuralNetwork.predict(spell);

            System.out.println(Arrays.toString(output));
            System.out.println(highestIndex(output));
        }

        int[] posCount = new int[spell_size / 2 + 3];
        int[] typeCount = new int[SPELL_DATA.length];

        int count = 0;

        while (count < 1000) {
            float[] input = new float[posCount.length * 2 + spell_size];

            for (int i = 0; i < input.length; i++) {
                input[i] = RandomGenerator.randomValue(-1, 1);
            }

            int indexCount = (input.length - spell_size) / 2;

            float[][] outputs = new float[indexCount][];
            int[] highestIndexes = new int[indexCount];
            Integer[] indexes = new Integer[indexCount];
            Integer[] indexesMid = new Integer[indexCount - 2];

            // collect outputs
            for (int i = 0; i < indexCount; i++) {
                float[] inputPart = new float[spell_size];

                System.arraycopy(input, i * 2, inputPart, 0, spell_size);

                outputs[i] = neuralNetwork.predict(inputPart);
                highestIndexes[i] = highestIndex(outputs[i]);
                indexes[i] = i;
            }

            System.arraycopy(indexes, 1, indexesMid, 0, indexCount - 2);

            Arrays.sort(indexesMid,
                    (a, b) -> -Float.compare(outputs[a][highestIndexes[a]], outputs[b][highestIndexes[b]]));


            Integer[] top = new Integer[3];

            System.arraycopy(indexesMid, 0, top, 0, top.length - 2);

            top[1] = 0;
            top[2] = indexCount - 1;

            Arrays.sort(top,
                    (a, b) -> -Float.compare(outputs[a][highestIndexes[a]], outputs[b][highestIndexes[b]]));

            if (outputs[top[top.length - 1]][highestIndexes[top[top.length - 1]]] > .25f) {

                Arrays.sort(top);

                for (int i = 0; i < top.length; i++) {

                    posCount[top[i]] += 1;
                    typeCount[highestIndexes[top[i]]] += 1;

                    System.out.println(top[i]);
                    System.out.println(highestIndexes[top[i]]);
                    System.out.println(outputs[top[i]][highestIndexes[top[i]]]);
                }

                System.out.println(Arrays.toString(input));
                System.out.println(Arrays.toString(posCount));
                System.out.println(Arrays.toString(typeCount));

                count++;
            }
        }

    }

    private static int highestIndex(float[] output) {
        int index = 0;

        for (int i = 1; i < output.length; i++) {
            if (output[i] > output[index]) {
                index = i;
            }
        }

        return index;
    }

}
