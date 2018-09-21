package com.raven.breakingsands.scenes.splashscreenscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.UICenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.MainMenuScene;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIFont;
import com.raven.engine2d.ui.UIImage;
import com.raven.engine2d.ui.UILabel;
import com.raven.engine2d.worldobject.Childable;

public class SplashScreenScene extends Scene<BreakingSandsGame> {

    public SplashScreenScene(BreakingSandsGame game) {
        super(game);

        UICenterContainer<SplashScreenScene> container = new UICenterContainer<>(this);
        addChild(container);

        UIImage<SplashScreenScene> splash = new UIImage<>(this, 314, 64, "sprites/armadillo.png");
        container.addChild(splash);

        container.pack();
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
