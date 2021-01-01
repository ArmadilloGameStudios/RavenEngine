package com.armadillogamestudios.storyteller.gameengine.scene.scenario;

import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.ui.UITextButtonStoryTeller;
import com.armadillogamestudios.storyteller.scenario.prompt.input.Input;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;

public class InputButton extends UITextButtonStoryTeller<ScenarioScene<? extends StoryTeller<?>>> {

    private Input input;

    public InputButton(ScenarioScene<? extends StoryTeller<?>> scene) {
        super(scene, "", "sprites/button_input.png", "inputbutton");

        UIFont font = this.getFont();
        font.setY(7);
        font.setSmall(true);
        font.setHighlight(false);
        font.setWrap(true);
    }

    @Override
    public void handleMouseClick() {
        System.out.println(this.getText());

        if (!this.isDisabled()) {
            this.getScene().getScenario().input(input);

            if (input.triggersPromptEvents()) {
                getScene().resetButtonPosition();
            }

            this.getScene().prompt();
        }
    }

    public void setInput(Input input) {
        this.input = input;

        if (input != null) {
            StringColored sc = input.getText();

            this.setText(sc.getString() + (input.getMinutes() == 0 ? "" : "\n(" + input.getMinutes() + "min)"));
            this.setColorFeed(sc.getColorFeed());
        } else {
            this.setText("");
        }

        if (input == null || input.metRequirements()) {
            this.setDisable(false);
        } else {
            this.setDisable(true);
        }

        load();
    }
}