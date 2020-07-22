package com.armadillogamestudios.breakingsands.scenes.splashscreenscene;

import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.ui.container.UICenterContainer;
import com.armadillogamestudios.breakingsands.BrokenMetalGame;
import com.armadillogamestudios.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.scene.Scene;
import com.armadillogamestudios.engine2d.ui.UIImage;

public class SplashScreenScene extends Scene<BrokenMetalGame> {

    private UICenterContainer<SplashScreenScene> container;

    public SplashScreenScene(BrokenMetalGame game) {
        super(game);
    }

    @Override
    public DrawStyle getDrawStyle() {
        return DrawStyle.ISOMETRIC;
    }

    @Override
    public void loadShaderTextures() {

    }

    @Override
    public void onEnterScene() {

        container = new UICenterContainer<>(this);
        addChild(container);

        UIImage<SplashScreenScene> splash = new UIImage<>(this, 314, 64, "sprites/armadillo.png");
        container.addChild(splash);

        container.pack();
    }

    @Override
    public void onExitScene() {

    }

    float time = 0;
    @Override
    public void onUpdate(float deltaTime) {
        time += deltaTime;

        if (time > 2000) {
            getGame().prepTransitionScene(new MainMenuScene(getGame()));
        }
    }

    @Override
    public void inputKey(int key, int action, int mods) {

    }
}
