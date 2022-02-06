package com.armadillogamestudios.totem.game.scene.splashscreen;

import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.totem.game.TotemGame;
import com.armadillogamestudios.totem.game.scene.TotemScene;

public class SplashScreenScene extends TotemScene {

    private final float fadeTime = 350f;
    private final float totalTime = 2500f;
    private UIImage<SplashScreenScene> splash;
    private float time = 0f;
    public SplashScreenScene(TotemGame game) {
        super(game);
    }

    @Override
    public void onUpdate(float deltaTime) {
        time += deltaTime;

        if (time < fadeTime) {
            splash.setFade(time / fadeTime);
        } else if (time > totalTime - fadeTime) {
            splash.setFade(1f - (time - (totalTime - fadeTime)) / fadeTime);
        } else {
            splash.setFade(1f);
        }

        if (time > totalTime) {
            getGame().prepTransitionScene(getGame().getMainMenuScene());
        }
    }

    @Override
    protected void loadUI() {

        UIContainer<SplashScreenScene> container = new UIContainer<>(this, UIContainer.Location.CENTER, UIContainer.Layout.VERTICAL);
        addChild(container);

        splash = new UIImage<>(this, 314, 64, "armadillo.png");
        container.addChild(splash);

        container.pack();
    }
}