package com.armadillogamestudios.mouseepic.scenes;

import com.armadillogamestudios.mouseepic.MouseEpicGame;
import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.graphics2d.DrawStyle;
import com.raven.engine2d.scene.Scene;

public class LoadWorldScene extends Scene<MouseEpicGame> {
    public LoadWorldScene(MouseEpicGame game) {
        super(game);
    }

    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }

    @Override
    public void loadShaderTextures() {

    }

    @Override
    public void onEnterScene() {

    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        getGame().prepTransitionScene(new WorldScene(getGame()));
    }

    @Override
    public void inputKey(int key, int action, int mods) {

    }
}
