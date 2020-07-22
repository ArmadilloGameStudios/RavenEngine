package com.armadillogamestudios.breakingsands.scenes.recordsscene;

import com.armadillogamestudios.engine2d.ui.container.UICenterContainer;
import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;

public class RecordsScene extends Scene<BrokenMetalGame> {

    private UIRecordsDisplay display;

    public RecordsScene(BrokenMetalGame game) {
        super(game);
    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.ISOMETRIC;
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
