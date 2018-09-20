package com.raven.breakingsands.scenes.recordsscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UILabel;
import com.raven.engine2d.worldobject.Childable;

public class RecordsScene extends Scene<BreakingSandsGame> {

    private UIRecordsDisplay display;

    public RecordsScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public void loadShaderTextures() {
        getEngine().getSpriteSheet("sprites/alphabet_small.png").load(this);
        getEngine().getSpriteSheet("sprites/alphabet.png").load(this);
    }

    @Override
    public void onEnterScene() {
        UICenterContainer<RecordsScene> centerContainer = new UICenterContainer<>(this);
        display = new UIRecordsDisplay(this);
        centerContainer.addChild(display);
        centerContainer.pack();
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
