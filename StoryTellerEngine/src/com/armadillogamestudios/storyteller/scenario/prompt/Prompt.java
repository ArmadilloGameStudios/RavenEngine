package com.armadillogamestudios.storyteller.scenario.prompt;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatable;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.actor.player.Player;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;
import com.armadillogamestudios.storyteller.stringformatter.StringFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Prompt implements Targetsable {

    private final Prompt.Type type;
    private final String text;
    private final List<Input> inputs = new ArrayList<>();
    private Resource[] targets;
    private InputEvent preInput, postInput, prePrompt;

    public Prompt(GameData gameData) {
        this.type = Type.List;
        this.text = gameData.getString("text");

        gameData.ifHas("pre_prompt", d -> prePrompt = new InputEvent(d, this));

        gameData.ifHas("pre_input", d -> preInput = new InputEvent(d, this));
        gameData.ifHas("post_input", d -> postInput = new InputEvent(d, this));

        gameData.getList("inputs").forEach(gdInput -> inputs.add(new GameDataInput(gdInput)));

    }

    public StringColored getText() {
        return StringFormatter.format(text, targets);
    }

    public Prompt.Type getType() {
        return type;
    }

    public List<Input> getInputs() {
        return inputs;
    }

    public void setTargets(Resource... targets) {
        this.targets = targets;

        inputs.forEach(input -> input.setTargets(targets));
    }

    public void triggerPreInputEvent() {
        if (preInput != null) {
            preInput.trigger();
        }
    }

    public void triggerPrePromptEvent() {
        if (prePrompt != null) {
            prePrompt.trigger();
        }
    }

    public void triggerPostInputEvent() {
        if (postInput != null) {
            postInput.trigger();
        }
    }

    @Override
    public Resource[] getTargets() {
        return targets;
    }

    public enum Type {
        List, Str
    }
}