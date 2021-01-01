package com.armadillogamestudios.storyteller.scenario.prompt.input;

import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;
import com.armadillogamestudios.storyteller.scenario.prompt.Targetsable;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;
import com.armadillogamestudios.storyteller.stringformatter.StringFormatter;

public abstract class Input implements Targetsable {

    private int minutes = 0;

    private String text;
    private Resource[] targets;

    public abstract boolean hasPrompt();

    public abstract Prompt nextPrompt();

    public abstract void triggerEvent();

    public abstract boolean metRequirements();

    public abstract boolean triggersPromptEvents();

    public final StringColored getText() {
        return StringFormatter.format(text, targets);
    }

    protected final void setText(String text) {
        this.text = text;
    }

    public final Resource[] getTargets() {
        return targets;
    }

    public final void setTargets(Resource... targets) {
        this.targets = targets;
    }

    public int getMinutes() {
        return minutes;
    }
}