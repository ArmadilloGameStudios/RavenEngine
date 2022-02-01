package com.armadillogamestudios.totem.game.scene.testspell;

import com.armadillogamestudios.engine2d.input.KeyboardHandler;
import com.armadillogamestudios.totem.game.TotemGame;
import com.armadillogamestudios.totem.game.scene.TotemScene;
import com.armadillogamestudios.totem.neuralnetwork.NeuralNetwork2;

import java.io.IOException;

public class TestSpellScene extends TotemScene {

    private NeuralNetwork2 neuralNetwork = null;

    public TestSpellScene(TotemGame game) {
        super(game);

        try {
            neuralNetwork = NeuralNetwork2.load("Totem\\nn.net");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    protected void loadUI() {
        SpellDrawing spellDrawing = new SpellDrawing(this);
        addChild(spellDrawing);
    }

    public NeuralNetwork2 getNeuralNetwork() {
        return neuralNetwork;
    }
}
