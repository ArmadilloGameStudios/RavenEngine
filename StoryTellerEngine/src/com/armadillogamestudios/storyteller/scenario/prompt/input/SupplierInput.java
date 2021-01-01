package com.armadillogamestudios.storyteller.scenario.prompt.input;

import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class SupplierInput extends Input {

    private Consumer<Resource[]> event;
    private Supplier<Prompt> prompt;

    public SupplierInput(String text, Consumer<Resource[]> event, Supplier<Prompt> prompt) {

        setText(text);

        this.event = event;
        this.prompt = prompt;
    }

    public void triggerEvent() {
        event.accept(getTargets());
    }

    @Override
    public boolean hasPrompt() {
        return true;
    }

    @Override
    public Prompt nextPrompt() {
        Prompt p = prompt.get();
        // p.setTargets(getTargets());
        return p;
    }

    @Override
    public boolean metRequirements() {
        return true;
    }
}
