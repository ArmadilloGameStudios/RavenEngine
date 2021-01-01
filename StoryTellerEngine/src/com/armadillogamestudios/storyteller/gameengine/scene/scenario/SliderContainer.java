package com.armadillogamestudios.storyteller.gameengine.scene.scenario;

import com.armadillogamestudios.engine2d.ui.*;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.ui.UITextButtonStoryTeller;
import com.armadillogamestudios.storyteller.resource.trait.TraitDescription;
import com.armadillogamestudios.storyteller.scenario.prompt.Prompt;
import com.armadillogamestudios.storyteller.scenario.prompt.input.Input;
import com.armadillogamestudios.storyteller.scenario.prompt.slider.PromptSlider;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SliderContainer extends UIContainer<ScenarioScene<?>> {

    private int selectorCount = 10;
    private ArrayList<UISelector2> selectors = new ArrayList<>(selectorCount);
    private PromptSlider prompt;
    private InputContinue inputContinue = new InputContinue();

    public SliderContainer(ScenarioScene<?> scene) {
        super(scene, Location.CENTER, Layout.VERTICAL);

        UIImage<ScenarioScene<?>> top = new UIImage<>(
                scene,
                116,
                4,
                "sprites/selectortop.png");

        addChild(top);

        UITextInput<ScenarioScene<?>> textInput = new UITextInput<>(scene, "enter your name", "sprites/input.png");
        textInput.setTextHighlight(scene.getGame().getTextHighlight());
        addChild(textInput);

        IntStream.range(0, selectorCount).forEach(this::createSlider);

        UIImage<ScenarioScene<?>> bot = new UIImage<>(
                scene,
                116,
                29,
                "sprites/selectorbutton.png");

        addChild(bot);

        // btn continue
        UITextButtonStoryTeller<ScenarioScene<?>> continueBtn = new UITextButtonStoryTeller<ScenarioScene<?>>(scene, "continue", "sprites/button_small.png", "smallbutton") {
            @Override
            public void handleMouseClick() {
                for (int i = 0; i < selectorCount; i++) {
                    prompt.getTargets()[0].addTrait(
                            prompt.getSliders().get(i).getTraitDescription().getKey(),
                            selectors.get(i).getValue());
                }

                prompt.getTargets()[0].setName(textInput.getText());

                inputContinue.setTargets(prompt.getTargets());
                this.getScene().getScenario().input(inputContinue);
                this.getScene().prompt();
            }
        };
        UIFont font = continueBtn.getFont();
        font.setY(7);
        font.setSmall(true);
        font.setHighlight(false);
        font.setWrap(true);

        continueBtn.load();
        addChild(continueBtn);

        pack();

        continueBtn.setX(51);
        continueBtn.setY(28);

        setY(getY() - 10f);
    }

    private void createSlider(int i) {

        UISelector2 red =
                new UISelector2(
                        getScene(),
                        "sprites/selector.png",
                        "sprites/selectormarker.png",
                        "sprites/selectorleftbutton.png",
                        "sprites/selectorrightbutton.png");

        red.setTextHighlight(getScene().getGame().getTextHighlight());

        selectors.add(red);

        addChild(red);
    }

    public void setPrompt(PromptSlider prompt) {

        this.prompt = prompt;

        AtomicInteger index = new AtomicInteger(0);

        prompt.getSliders().forEach(slider -> {
            UISelector2 selector = selectors.get(index.get());
            TraitDescription traitDescription = slider.getTraitDescription();

            selector.setTitle(traitDescription.getName());
            selector.setDisplays(traitDescription.getProperties());

            index.incrementAndGet();
        });
    }

    private class InputContinue extends Input {

        @Override
        public boolean hasPrompt() {
            return true;
        }

        @Override
        public Prompt nextPrompt() {
            Prompt next = prompt.getPrompt();
            next.setTargets(this.getTargets());

            return next;
        }

        @Override
        public void triggerEvent() {

        }

        @Override
        public boolean metRequirements() {
            return true;
        }

        @Override
        public boolean triggersPromptEvents() {
            return false;
        }
    }
}

