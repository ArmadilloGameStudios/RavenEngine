package com.armadillogamestudios.storyteller.scenario.prompt.slider;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;

public class SliderString extends Slider {

    public SliderString(String key) {
        super(key);
    }

    @Override
    public boolean metRequirements() {
        return true;
    }
}
