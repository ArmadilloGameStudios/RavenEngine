package com.raven.breakingsands.scenes.battlescene.victory;

import com.raven.breakingsands.character.Augmentation;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;

public class HUDAugmentation extends HUDButton<BattleScene, VictoryDisplay> {

    private Augmentation augmentation;

    public HUDAugmentation(BattleScene scene, Augmentation augmentation) {
        super(scene, "");

        this.augmentation = augmentation;

        setText(augmentation.getName());
    }

    @Override
    public float getHeight() {
        return 70f;
    }

    @Override
    public float getWidth() {
        return 131f;
    }

    @Override
    public void handleMouseClick() {
        getParent().selectAugmentation(augmentation);
    }
}
