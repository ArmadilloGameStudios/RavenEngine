package com.armadillogamestudios.storyteller.gameengine.scene.scenario;

import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.scenario.prompt.input.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.input.PromptInput;
import com.armadillogamestudios.storyteller.scenario.prompt.input.SupplierInput;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InputContainer extends UIContainer<ScenarioScene<?>> {

    private final int buttonCount = 7;
    private int buttonStart = 0;

    private InputButton[] inputButtons = new InputButton[buttonCount];

    private boolean showDisabled = false;

    public InputContainer(ScenarioScene<?> scene) {
        super(scene, UIContainer.Location.BOTTOM, UIContainer.Layout.VERTICAL);

        setIncludeInvisible(true);

        for (int i = 0; i < buttonCount; i++) {
            inputButtons[i] = new InputButton(scene);
            addChild(inputButtons[i]);
        }
    }

    public void setPrompt(PromptInput prompt) {
        List<Input> inputs;

        if (showDisabled) {
            inputs = new ArrayList<>(prompt.getInputs());
        } else {
            inputs = prompt.getInputs().stream().filter(Input::metRequirements).collect(Collectors.toList());
        }

        int iStart = 0;

        if (buttonStart > 0) {
            iStart = 1;

            inputButtons[0].setVisibility(true);
            inputButtons[0].setInput(new BackInput());
        }

        if (inputs.size() - buttonStart - iStart == buttonCount) {

            for (int i = iStart; i < buttonCount; i++) {
                inputButtons[i].setVisibility(true);
                inputButtons[i].setInput(inputs.get(i - iStart + buttonStart));
            }

        } else if (inputs.size() - buttonStart < buttonCount) {

            for (int i = iStart; i < buttonCount; i++) {
                if (inputs.size() > i - iStart + buttonStart) {
                    inputButtons[i].setVisibility(true);
                    inputButtons[i].setInput(inputs.get(i - iStart + buttonStart));
                } else {
                    inputButtons[i].setVisibility(false);
                    inputButtons[i].setInput(null);
                }
            }

        } else {

            for (int i = iStart; i < buttonCount; i++) {
                if (i == buttonCount - 1) {
                    inputButtons[i].setVisibility(true);
                    inputButtons[i].setInput(new MoreInput());
                } else {
                    inputButtons[i].setVisibility(true);
                    inputButtons[i].setInput(inputs.get(i - iStart + buttonStart));
                }
            }

        }

        pack();
    }

    public void resetButtonPosition() {
        buttonStart = 0;
    }

    class MoreInput extends SupplierInput {

        public MoreInput() {
            super(
                    "more...",
                    (targets) -> buttonStart += buttonStart == 0 ? buttonCount - 1 : buttonCount - 2,
                    () -> getScene().getScenario().getPrompt());
        }

        @Override
        public boolean triggersPromptEvents() {
            return false;
        }
    }

    class BackInput extends SupplierInput {

        public BackInput() {
            super(
                    "back...",
                    (targets) -> buttonStart -= buttonStart == buttonCount - 1 ? buttonCount - 1 : buttonCount - 2,
                    () -> getScene().getScenario().getPrompt());
        }

        @Override
        public boolean triggersPromptEvents() {
            return false;
        }
    }
}
