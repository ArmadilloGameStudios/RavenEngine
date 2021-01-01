package com.armadillogamestudios.storyteller.gameengine.scene.scenario;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.ui.UIFont;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.container.*;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;
import com.armadillogamestudios.storyteller.gameengine.ui.UILabelStoryTeller;
import com.armadillogamestudios.storyteller.scenario.Scenario;
import com.armadillogamestudios.storyteller.scenario.prompt.input.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;
import com.armadillogamestudios.storyteller.scenario.prompt.input.PromptInput;
import com.armadillogamestudios.storyteller.scenario.prompt.input.SupplierInput;
import com.armadillogamestudios.storyteller.scenario.prompt.slider.PromptSlider;
import com.armadillogamestudios.storyteller.stringformatter.StringColored;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class ScenarioScene<G extends StoryTeller<G>>
        extends SceneStoryTeller<G> {

    private Scenario scenario;
    private UIContainer<ScenarioScene<?>> promptContainer, traitContainer;
    private InputContainer inputContainer;
    private SliderContainer sliderContainer;

    private UIImage<ScenarioScene<?>> promptBackground;
    private UILabelStoryTeller<ScenarioScene<?>> promptText, traitText;

    public ScenarioScene(G game, boolean newGame) {
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
        promptContainer = new UIContainer<>(this, UIContainer.Location.UPPER, UIContainer.Layout.VERTICAL);
        addChild(promptContainer);

        promptBackground = new UIImage<>(this, 506, 106, "sprites/prompt.png");
        promptContainer.addChild(promptBackground);

        promptText = new UILabelStoryTeller<>(this, "", 500, 100);
        UIFont font = promptText.getFont();
        font.setSmall(true);
        font.setHighlight(false);
        promptText.load();
        promptContainer.addChild(promptText);

        promptContainer.pack();

        promptText.setY(promptText.getY() + 101);
        promptText.setX(promptText.getX() + 8);

        // input
        inputContainer = new InputContainer(this);
        addChild(inputContainer);

        // input
        sliderContainer = new SliderContainer(this);
        addChild(sliderContainer);

        // trait
        traitContainer = new UIContainer<>(this, UIContainer.Location.LEFT, UIContainer.Layout.VERTICAL);
        addChild(traitContainer);

        traitText = new UILabelStoryTeller<>(this, "", 500, 200);
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

        if (prompt.getType() == Prompt.Type.SLIDER) {
            inputContainer.setVisibility(false);
            sliderContainer.setVisibility(true);
            sliderContainer.setPrompt((PromptSlider) prompt);
        } else if (prompt.getType() == Prompt.Type.INPUT) {
            sliderContainer.setVisibility(false);
            inputContainer.setVisibility(true);
            inputContainer.setPrompt((PromptInput) prompt);
        }
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }

    public final Scenario getScenario() {
        return scenario;
    }

    public void resetButtonPosition() {
        inputContainer.resetButtonPosition();
    }
}
