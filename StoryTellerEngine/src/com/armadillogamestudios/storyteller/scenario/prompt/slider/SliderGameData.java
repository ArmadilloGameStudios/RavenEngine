package com.armadillogamestudios.storyteller.scenario.prompt.slider;

import com.armadillogamestudios.engine2d.database.GameData;

public class SliderGameData extends Slider {
    public SliderGameData(GameData gameData) {
        super(gameData.getString("trait"));
    }

    @Override
    public boolean metRequirements() {
        return false;
    }
}
