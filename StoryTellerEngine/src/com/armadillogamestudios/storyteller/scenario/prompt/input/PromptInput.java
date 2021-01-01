package com.armadillogamestudios.storyteller.scenario.prompt.input;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

public class PromptInput extends Prompt {

    private List<Input> inputs = new ArrayList<>();
    private Resource[] targets;

    public PromptInput(GameData gameData) {
        super(gameData);

        gameData.getList("inputs").forEach(gdInput -> inputs.add(new GameDataInput(gdInput)));
    }

    public List<Input> getInputs() {
        return inputs;
    }

    @Override
    public Type getType() {
        return Type.INPUT;
    }

    @Override
    public Resource[] getTargets() {
        return targets;
    }

    @Override
    public void setTargets(Resource... targets) {
        this.targets = targets;

        inputs.forEach(input -> input.setTargets(targets));
    }
}
