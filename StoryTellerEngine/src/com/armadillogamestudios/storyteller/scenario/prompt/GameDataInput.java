package com.armadillogamestudios.storyteller.scenario.prompt;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class GameDataInput extends Input {

    private String prompt;
    private Supplier<String> interruption;
    private List<InputEvent> events = new ArrayList<>();
    private InputRequirement requirement;

    public GameDataInput(GameData gameData) {

        setText(gameData.getString("text"));

        gameData.ifHas("events", (gd) -> gd.asList().forEach(event ->  events.add(new InputEvent(event, this))));


        gameData.ifHas("requirement", (d) -> {
            requirement = InputRequirement.create(d, this);
        });

        gameData.ifHas("prompt", (d) -> this.prompt = d.asString());

        gameData.throwExceptionIfInvalid("requirements");
    }

    @Override
    public boolean hasPrompt() {
        return prompt != null;
    }

    @Override
    public Prompt nextPrompt() {
        Prompt prompt = StoryTeller.getPromptManager().get(this.prompt);

        if (prompt == null) {
            System.out.println("Can't find prompt " + this.prompt);
            throw new NullPointerException("Can't find prompt " + this.prompt);
        }

        prompt.setTargets(getTargets());
        return prompt;
    }

    public void triggerEvent() {
        events.forEach(InputEvent::trigger);
    }

    @Override
    public boolean metRequirements() {
        return requirement == null || requirement.met();
    }

    @Override
    public boolean triggersPromptEvents() {
        return true;
    }
}
