package com.armadillogamestudios.storyteller.gameengine.scene.splashscreen;

import com.armadillogamestudios.engine2d.graphics2d.DrawStyle;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.game.StoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.SceneStoryTeller;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;

public class SplashScreenScene<S extends StoryTeller<S>> extends SceneStoryTeller<S> {

    private UIImage<SplashScreenScene<?>> splash;

    public SplashScreenScene(S story) {
        super(story);

        UIContainer<SplashScreenScene<?>> container = new UIContainer<>(this, UIContainer.Location.CENTER, UIContainer.Layout.VERTICAL);
        addChild(container);

        splash = new UIImage<>(this, 314, 64, "sprites/armadillo.png");
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

    private float time = 0f;
    private final float fadeTime = 350f;
    private final float totalTime = 2500f;

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
    public DrawStyle getDrawStyle() {
        return DrawStyle.STANDARD;
    }
}
