package com.raven.breakingsands.scenes.settingsscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.DisplayPawn;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.Game;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIButton;
import com.raven.engine2d.ui.UISelector;
import com.raven.engine2d.ui.UITextButton;
import com.raven.engine2d.util.math.Vector2i;
import sun.java2d.HeadlessGraphicsEnvironment;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

        List<Vector2i> list = GameProperties.getResolutionList();
        List<String> strings = new ArrayList<>();
        list.forEach(v -> {
            strings.add(v.x + " x " + v.y);
        });

        UISelector<SettingsScene, Vector2i> resolutionSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "resolution",
                list, strings,
                list.get(0));
        container.addChild(resolutionSel);

        List<Integer> scales = Arrays.asList(1, 2, 3, 4, 5);
        List<String> scalesStrings = Arrays.asList("1", "2", "3", "4", "5");
        UISelector<SettingsScene, Integer> scaleSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "pixel scaling",
                scales, scalesStrings,
                scales.get(1));
        container.addChild(scaleSel);

        List<Integer> volume = Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
        List<String> volumeStrings = Arrays.asList("0", "10", "20", "30", "40", "50", "60", "70", "80", "90", "100");
        UISelector<SettingsScene, Integer> musicVolumeSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "music volume",
                volume, volumeStrings,
                volume.get(10));
        container.addChild(musicVolumeSel);

        UISelector<SettingsScene, Integer> sfxVolumeSel = new UISelector<>(this,
                "sprites/selector.png",
                "sprites/selectorleftbutton.png",
                "sprites/selectorrightbutton.png",
                "sfx volume",
                volume, volumeStrings,
                volume.get(10));
        container.addChild(sfxVolumeSel);

        UITextButton<SettingsScene> doneBtn = new UITextButton<SettingsScene>(this, "done", "sprites/button.png", "mainbutton") {
            @Override
            public void handleMouseClick() {
                GameProperties.setScaling(scaleSel.getValue());
                GameProperties.setMusicVolume(musicVolumeSel.getValue());
                GameProperties.setSFXVolume(sfxVolumeSel.getValue());

                Vector2i dim = resolutionSel.getValue();
                getEngine().getWindow().setDimension(dim.x, dim.y);

                getGame().prepTransitionScene(new MainMenuScene(getGame()));
            }
        };
        doneBtn.load();
        container.addChild(doneBtn);

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
