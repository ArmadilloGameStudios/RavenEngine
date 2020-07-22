package com.armadillogamestudios.storyteller.gameengine.scene.scenario;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.container.*;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.storyteller.gameengine.StoryTeller;
import com.armadillogamestudios.storyteller.scenario.Scenario;
import com.armadillogamestudios.storyteller.scenario.prompt.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;
import com.armadillogamestudios.storyteller.scenario.prompt.SupplierInput;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioScene<S extends StoryTeller<S>>
        extends Scene<S> {

    private Scenario scenario;
    private UIContainer<ScenarioScene<?>> promptContainer, inputContainer, traitContainer;

    private final int buttonCount = 7;
    private int buttonStart = 0;
    private InputButton[] inputButtons = new InputButton[buttonCount];

    private boolean showDisabled = false;

    private UILabel<ScenarioScene<?>> promptText, traitText;

    public ScenarioScene(S game, boolean newGame) {
        super(game);

        if (newGame) {
            scenario = game.startScenario();
        } else {

        }
    }

    @Override
    public void loadShaderTextures() {

    }

    @Override
    public void onEnterScene() {

        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        // UI
        // prompt
        promptContainer = new UIContainer<>(this, UIContainer.Location.UPPER, UIContainer.Layout.HORIZONTAL);
        addChild(promptContainer);

        promptText = new UILabel<>(this, "", 500, 100);
        UIFont font = promptText.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        promptText.load();
        promptContainer.addChild(promptText);

        promptContainer.pack();

        // input
        inputContainer = new UIContainer<>(this, UIContainer.Location.BOTTOM, UIContainer.Layout.VERTICAL);
        inputContainer.setIncludeInvisible(true);
        addChild(inputContainer);

        for (int i = 0; i < buttonCount; i++) {
            inputButtons[i] = new InputButton(this);
            inputContainer.addChild(inputButtons[i]);
        }

        // trait
        traitContainer = new UIContainer<>(this, UIContainer.Location.LEFT, UIContainer.Layout.VERTICAL);
        addChild(traitContainer);

        traitText = new UILabel<>(this, "", 500, 200);
        font = traitText.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        font.setWrap(true);
        traitText.load();
        traitContainer.addChild(traitText);

        traitContainer.pack();

        // Prompt
        prompt();
    }

    void prompt() {
        Prompt prompt = scenario.getPrompt();
        StringColored sc = prompt.getText();
        promptText.setText(sc.getString());
        promptText.setColorFeed(sc.getColorFeed());
        promptText.load();

        traitText.setText(prompt.getTargets()[0].getDescription());
        traitText.load();

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

        inputContainer.pack();
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void inputKey(int key, int action, int mods) {

    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }

    public final Scenario getScenario() {
        return scenario;
    }

    public void resetButtonPosition() {
        buttonStart = 0;
    }

    class MoreInput extends SupplierInput {

        public MoreInput() {
            super(
                    "more...",
                    (targets) -> buttonStart += buttonStart == 0 ? buttonCount - 1 : buttonCount - 2,
                    () -> scenario.getPrompt());
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
                    () -> scenario.getPrompt());
        }

        @Override
        public boolean triggersPromptEvents() {
            return false;
        }
    }
}
