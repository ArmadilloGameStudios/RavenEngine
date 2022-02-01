package com.armadillogamestudios.totem.neuralnetwork;

import com.armadillogamestudios.engine2d.util.math.Vector2f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Transformation {

    static private float[] rotations = new float[] {
            (float) (2f * Math.PI / 12.0f),
            (float) (2f * Math.PI * 2f / 12.0f),
            (float) (2f * Math.PI * 3f / 12.0f),
            (float) (2f * Math.PI * 4f / 12.0f),
            (float) (2f * Math.PI * 5f / 12.0f),
            (float) (2f * Math.PI * 6f / 12.0f),
            (float) (2f * Math.PI * 7f / 12.0f),
            (float) (2f * Math.PI * 8f / 12.0f),
            (float) (2f * Math.PI * 9f / 12.0f),
            (float) (2f * Math.PI * 10f / 12.0f),
            (float) (2f * Math.PI * 11f / 12.0f),
    };

    static private final Vector2f old = new Vector2f();
    static private final Vector2f in = new Vector2f();
    static private final Vector2f out = new Vector2f();

    public static float[] normalizeAndAddAngles(float[] spell) {
        float[] normalizedSpellPart = new float[spell.length * 2];

        old.x = 0;
        old.y = 1;

        for (int i = 0; i < spell.length / 2; i++) {
            in.x = spell[i * 2];
            in.y = spell[i * 2 + 1];

            in.normalize(out);

            normalizedSpellPart[i * 4] = out.x;
            normalizedSpellPart[i * 4 + 1] = out.y;

            in.x = out.x;
            in.y = out.y;

            float theta = (float) Math.acos(old.x * in.x + old.y * in.y);

            if (Float.isNaN(theta))
                theta = 0;

            out.x = (float) (Math.cos(theta) * old.x - Math.sin(theta) * old.y);
            out.y = (float) (Math.sin(theta) * old.x + Math.cos(theta) * old.y);

            normalizedSpellPart[i * 4 + 2] = out.x;
            normalizedSpellPart[i * 4 + 3] = out.y;

            old.x = in.x;
            old.y = in.y;
        }

        return normalizedSpellPart;
    }

    public static List<InputTargetPair> spell(float[][] spellData) {
        List<float[]> normalizedSpellPartList = new ArrayList<>();

        for (float[] s : spellData) {
            normalizedSpellPartList.add(normalizeAndAddAngles(s));
        }

        List<InputTargetPair> inputTargetPairList = new ArrayList<>();

        for (int i = 0; i < normalizedSpellPartList.size(); i++) {

            float[] targets = new float[normalizedSpellPartList.size()];
            targets[i] = 1;

            float[] fullSpell = new float[spellData[0].length * 2];
            System.arraycopy(normalizedSpellPartList.get(i), 0, fullSpell, 0, spellData[0].length * 2);

            InputTargetPair base = new InputTargetPair(fullSpell, targets);
            inputTargetPairList.add(base);

            for (int r = 0; r < rotations.length; r++) {
                float[] rotatedSpell = new float[spellData[0].length * 2];

                System.arraycopy(fullSpell, 0, rotatedSpell, 0, spellData[0].length * 2);

                old.x = 0;
                old.y = 1;

                rotatedSpell[2] = (float) (Math.cos(rotations[r]) * old.x - Math.sin(rotations[r]) * old.y);
                rotatedSpell[3] = (float) (Math.sin(rotations[r]) * old.x + Math.cos(rotations[r]) * old.y);

                for (int l = 0; l < 6; l++) {
                    old.x = rotatedSpell[l * 2];
                    old.y = rotatedSpell[l * 2 + 1];

                    rotatedSpell[l * 2] = (float) (Math.cos(rotations[r]) * old.x - Math.sin(rotations[r]) * old.y);
                    rotatedSpell[l * 2 + 1] = (float) (Math.sin(rotations[r]) * old.x + Math.cos(rotations[r]) * old.y);
                }

                InputTargetPair rotated = new InputTargetPair(rotatedSpell, targets);

                inputTargetPairList.add(rotated);
            }
        }

        return inputTargetPairList;
    }

    public static Collection<MLData> random(int count, int targetSize) {
        List<MLData> data = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            float[] input = new float[64];

            for (int j = 0; j < input.length; j++) {
                input[j] = RandomGenerator.randomValue(-1, 1);
            }

            data.add(new MLData(normalizeAndAddAngles(input), new float[targetSize]));
        }

        return data;
    }

    public static List<MLData> randomRotation(List<MLData> data, float maxRotation, float maxRotationAccuracy) {
        List<MLData> rotatedData = new ArrayList<>(data.size());

        int partCount = data.get(0).getInputs().length / 4;

        for (MLData d : data) {
            if (.5f > RandomGenerator.random.nextFloat()) {
                float accuracy = 0;
                float[] inputs = new float[d.getInputs().length];
                System.arraycopy(d.getInputs(), 0, inputs, 0, d.getInputs().length);

                for (int part = 0; part < partCount; part++) {

                    // get rotation/accuracy
                    float rand = RandomGenerator.random.nextFloat();
                    float sign = Math.signum(rand - .5f);
                    rand *= rand;

                    accuracy += (1 - maxRotationAccuracy * rand) / (float) partCount;

                    float theta = ((float) Math.PI) * 2f * maxRotation * rand * sign;

                    // calc new angles
                    old.x = inputs[part];
                    old.y = inputs[part + 1];

                    inputs[part] = (float) (Math.cos(theta) * old.x - Math.sin(theta) * old.y);
                    inputs[part + 1] = (float) (Math.sin(theta) * old.x + Math.cos(theta) * old.y);

                    old.x = inputs[part + 2];
                    old.y = inputs[part + 3];

                    inputs[part + 2] = (float) (Math.cos(theta) * old.x - Math.sin(theta) * old.y);
                    inputs[part + 3] = (float) (Math.sin(theta) * old.x + Math.cos(theta) * old.y);
                }

                float[] targets = new float[d.getTargets().length];
                System.arraycopy(d.getTargets(), 0, targets, 0, d.getTargets().length);

                for (int i = 0; i < targets.length; i++) {
                    targets[i] *= accuracy;
                }

                rotatedData.add(new MLData(inputs, targets));
            } else {
                rotatedData.add(d);
            }
        }

        return rotatedData;
    }
}
