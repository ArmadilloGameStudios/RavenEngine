package com.raven.breakingsands.scenes.settingsscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UISelector;
import com.raven.engine2d.ui.UITextButton;
import com.raven.engine2d.util.math.Vector2i;

import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SettingsScene extends Scene<BreakingSandsGame> {

    public SettingsScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public void loadShaderTextures() {

    }

    @Override
    public void onEnterScene() {

        UICenterContainer<SettingsScene> container = new UICenterContainer<>(this);
        addChild(container);

        // resolution
        List<Vector2i> list = GameProperties.getResolutionList();
        List<String> strings = new ArrayList<>();
        list.forEach(v -> strings.add(v.x + " x " + v.y));

        AtomicReference<Vector2i> selected = new AtomicReference<>(list.get(0));
        list.stream()
                .filter(l -> l.x == GameProperties.getScreenWidth() && l.y == GameProperties.getScreenHeight())
                .findFirst()
                .ifPresent(selected::set);

        UISelector<SettingsScene, Vector2i> resolutionSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "resolution",
                list, strings,
                selected.get());
        container.addChild(resolutionSel);

        // scale
        List<Integer> scales = Arrays.asList(1, 2, 3, 4, 5);
        List<String> scalesStrings = Arrays.asList("1", "2", "3", "4", "5");

        AtomicReference<Integer> selectedScale = new AtomicReference<>(scales.get(1));
        scales.stream()
                .filter(l -> l.equals(GameProperties.getScaling()))
                .findFirst()
                .ifPresent(selectedScale::set);

        UISelector<SettingsScene, Integer> scaleSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "pixel scaling",
                scales, scalesStrings,
                selectedScale.get());
        container.addChild(scaleSel);

        // music
        List<Integer> volume = Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        List<String> volumeStrings = Arrays.asList("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");

        AtomicReference<Integer> selectedMusic = new AtomicReference<>(volume.get(10));
        volume.stream()
                .filter(l -> l >= GameProperties.getMusicVolume())
                .findFirst()
                .ifPresent(selectedMusic::set);

        UISelector<SettingsScene, Integer> musicVolumeSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "music volume",
                volume, volumeStrings,
                selectedMusic.get());
        container.addChild(musicVolumeSel);

        // sfx

        AtomicReference<Integer> selectedSfx = new AtomicReference<>(volume.get(10));
        volume.stream()
                .filter(l -> l >= GameProperties.getSFXVolume())
                .findFirst()
                .ifPresent(selectedSfx::set);

        UISelector<SettingsScene, Integer> sfxVolumeSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "sfx volume",
                volume, volumeStrings,
                selectedSfx.get());
        container.addChild(sfxVolumeSel);

        UITextButton<SettingsScene> doneBtn = new UITextButton<SettingsScene>(this, "apply", "sprites/button.png", "mainbutton") {
            @Override
            public void handleMouseClick() {
                GameProperties.setScaling(scaleSel.getValue());
                GameProperties.setMusicVolume(musicVolumeSel.getValue());
                GameProperties.setSFXVolume(sfxVolumeSel.getValue());

                Vector2i dim = resolutionSel.getValue();
                getEngine().getWindow().setDimension(dim.x, dim.y);

                getGame().saveSettings();

                getGame().prepTransitionScene(new SettingsScene(getGame()));
            }
        };
        doneBtn.load();
        container.addChild(doneBtn);

        UITextButton<SettingsScene> exitBtn = new UITextButton<SettingsScene>(this, "main menu", "sprites/button.png", "mainbutton") {
            @Override
            public void handleMouseClick() {
                getGame().prepTransitionScene(new MainMenuScene(getGame()));
            }
        };
        exitBtn.load();
        container.addChild(exitBtn);

        container.pack();
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
}
