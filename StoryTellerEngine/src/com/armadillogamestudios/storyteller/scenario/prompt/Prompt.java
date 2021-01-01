package com.armadillogamestudios.storyteller.scenario.prompt;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.input.InputEvent;
import com.armadillogamestudios.storyteller.scenario.prompt.input.PromptInput;
import com.armadillogamestudios.storyteller.scenario.prompt.slider.PromptSlider;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;
import com.armadillogamestudios.storyteller.stringformatter.StringFormatter;

public abstract class Prompt implements Targetsable {

    private final String text;
    private InputEvent preInput, postInput, prePrompt;

    public Prompt(GameData gameData) {
        this.text = gameData.getString("text");

        gameData.ifHas("pre_prompt", d -> prePrompt = new InputEvent(d, this));

        gameData.ifHas("pre_input", d -> preInput = new InputEvent(d, this));
        gameData.ifHas("post_input", d -> postInput = new InputEvent(d, this));
    }

    public static Prompt create(GameData gameData) {
        if (gameData.has("sliders")) {
            return new PromptSlider(gameData);
        } else {
            return new PromptInput(gameData);
        }
    }

    public StringColored getText() {
        return StringFormatter.format(text, getTargets());
    }

    public abstract void setTargets(Resource... targets);

    public abstract Prompt.Type getType();

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

    public enum Type {
        INPUT, SLIDER
    }
}

