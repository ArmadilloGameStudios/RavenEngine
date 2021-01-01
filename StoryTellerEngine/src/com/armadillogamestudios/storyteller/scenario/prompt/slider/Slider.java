package com.armadillogamestudios.storyteller.scenario.prompt.slider;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.Resource;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;
import com.armadillogamestudios.storyteller.scenario.prompt.Targetsable;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;
import com.armadillogamestudios.storyteller.stringformatter.StringFormatter;

public abstract class Slider implements Targetsable {

    private TraitDescription traitDescription;
    private String text;
    private Resource[] targets;

    public Slider(String key) {
        this.traitDescription = StoryTeller.getTraitManager().get(key);
    }

    public static Slider create(GameData gameData) {
        if (gameData.isString()) {
            return new SliderString(gameData.asString());
        } else {
            return new SliderGameData(gameData);
        }
    }

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

    public TraitDescription getTraitDescription() {
        return traitDescription;
    }

    public abstract boolean metRequirements();
}
