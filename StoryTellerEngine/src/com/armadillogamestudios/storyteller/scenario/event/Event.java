package com.armadillogamestudios.storyteller.scenario.event;

import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Event<R extends Resource> {
    private long triggerTime;
    private Supplier<Prompt> event;
    private R target;
    private Consumer<R> init;

    public Event(R target, long triggerTime) {
        this.triggerTime = triggerTime;
    }

    protected void setEvent(Supplier<Prompt> event) {
        this.event = event;
    }

    public Prompt getPrompt() {
        return event.get();
    }

    protected void setInit(Consumer<R> init) {
        this.init = init;
    }

    public void doInit() {
        init.accept(target);
    }

    public long getTriggerTime() {
        return triggerTime;
    }

}