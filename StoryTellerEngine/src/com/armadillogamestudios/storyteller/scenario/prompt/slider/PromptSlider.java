package com.armadillogamestudios.storyteller.scenario.prompt.slider;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;

public class PromptSlider extends Prompt {

    private List<Slider> sliders = new ArrayList<>();
    private String prompt;
    private Resource[] targets;

    public PromptSlider(GameData gameData) {
        super(gameData);

        gameData.getList("sliders").forEach(gdSlider -> sliders.add(Slider.create(gdSlider)));
        prompt = gameData.getString("prompt");
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    @Override
    public Type getType() {
        return Type.SLIDER;
    }

    @Override
    public Resource[] getTargets() {
        return targets;
    }

    @Override
    public void setTargets(Resource... targets) {
        this.targets = targets;

        sliders.forEach(slider -> slider.setTargets(targets));
    }

    public Prompt getPrompt() {
        return StoryTeller.getPromptManager().get(prompt);
    }
}
