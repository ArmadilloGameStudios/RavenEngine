package com.armadillogamestudios.storyteller.scenario;

import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import com.armadillogamestudios.storyteller.resource.actor.player.Player;
import com.armadillogamestudios.storyteller.resource.location.World;
import com.armadillogamestudios.storyteller.scenario.event.Event;
import com.armadillogamestudios.storyteller.scenario.prompt.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Scenario {
    private List<Input> activeInputs = new ArrayList<>();
    private Prompt activePrompt;
    private Player player;
    private World world;

    public Scenario(Player player, World world) {
        this.player = player;
        this.world = world;
    }

    public final void start() {
        while (activePrompt != null) getPrompt();
    }

    public final Prompt getPrompt() {
        return activePrompt;
    }

    public boolean isValidChoice(int choice) {
        System.out.println(choice);
        return choice > 0 && choice <= activeInputs.size();
    }

    public final boolean input(Input i) {

        boolean triggerPromptEvents = i.triggersPromptEvents();

        Event<?> event = world.passTime(i.getMinutes());

        if (event == null) {
            if (triggerPromptEvents) activePrompt.triggerPreInputEvent();
            i.triggerEvent();
            if (triggerPromptEvents) activePrompt.triggerPostInputEvent();

            if (i.hasPrompt()) {
                activePrompt = i.nextPrompt();
            } else
                activePrompt = player.nextPrompt();
        } else {
            event.doInit();

            if (triggerPromptEvents) activePrompt.triggerPostInputEvent();
            activePrompt = event.getPrompt();
        }

        if (triggerPromptEvents) activePrompt.triggerPrePromptEvent();

        return true;
    }

    protected void setActivePrompt(Prompt prompt) {
        this.activePrompt = prompt;
    }

    protected void setActivePrompt(String prompt) {
        this.activePrompt = StoryTeller.getPromptManager().get(prompt);
    }

    public boolean input(String choice) {
        // TODO

        return true;
    }

    public boolean isValidChoice(String choice) {
        System.out.println(choice);
        return choice.trim().length() > 0;
    }

    public Player getPlayer() {
        return player;
    }

    public World getWorld() {
        return world;
    }
}