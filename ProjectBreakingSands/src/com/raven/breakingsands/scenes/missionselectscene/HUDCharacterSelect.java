package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.scenes.hud.HUDBottomContainer;
import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.engine.worldobject.Childable;
import com.raven.engine.worldobject.HUDObject;

public class HUDCharacterSelect extends HUDBottomContainer<MissionSelectScene> {

    private HUDObject leftButton, rightButton;
    private HUDCharacterDisplay display;

    public HUDCharacterSelect(MissionSelectScene scene) {
        super(scene);

        display = new HUDCharacterDisplay(getScene());

        leftButton = new HUDButton<MissionSelectScene, HUDCharacterSelect>(scene, "<") {
            @Override
            public float getHeight() {
                return 75;
            }

            @Override
            public float getWidth() {
                return 75;
            }

            @Override
            public void handleMouseClick() {
                leftButtonClicked();
            }
        };

        rightButton = new HUDButton<MissionSelectScene, HUDCharacterSelect>(scene, ">") {
            @Override
            public float getHeight() {
                return 75;
            }

            @Override
            public float getWidth() {
                return 75;
            }

            @Override
            public void handleMouseClick() {
                rightButtonClicked();
            }
        };

        addChild(leftButton);
        addChild(display);
        addChild(rightButton);
    }

    private void leftButtonClicked() {
        getScene().selectCharacterLeft();
    }

    private void rightButtonClicked() {
        getScene().selectCharacterRight();
    }

    public void setText(String text) {
        display.setText(text);
    }
}
