package com.armadillogamestudios.crashlanding.ui;

import com.armadillogamestudios.crashlanding.CrashLandingGame;
import com.armadillogamestudios.engine2d.GameEngine;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimation;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.UIObject;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;

import java.util.Random;

public class CrashLandingMainMenuDecor extends UIContainer<MainMenuScene<CrashLandingGame>> {

    private float xOffset = (float) GameProperties.getDisplayWidth() / GameProperties.getScaling();
    private float yOffset = (float) GameProperties.getDisplayHeight() / GameProperties.getScaling();

    private Random random = new Random();

    private final int size = 300;
    private final float max = ((yOffset - size) / 2f);

    private final UIImage<MainMenuScene<CrashLandingGame>>
            dust1_1, dust1_2, dust2_1, dust2_2, dust3_1, dust3_2,
            planet, ship;

    public CrashLandingMainMenuDecor(MainMenuScene<CrashLandingGame> scene) {
        super(scene, Location.CENTER, Layout.HORIZONTAL);

        UIImage<MainMenuScene<CrashLandingGame>> outline = new UIImage<>(scene, size, size,
                "sprites/main menu background outline.png");

        addChild(outline);
        outline.setZ(.9f);
        centerUIObject(outline, 0);

        UIImage<MainMenuScene<CrashLandingGame>> outlineBuffer1 = new UIImage<>(scene, size, size,
                "sprites/main menu background outline buffer.png");

        addChild(outlineBuffer1);
        outlineBuffer1.setZ(.9f);
        centerUIObject(outlineBuffer1, 0);
        outlineBuffer1.setY(outlineBuffer1.getY() + size);

        UIImage<MainMenuScene<CrashLandingGame>> outlineBuffer2 = new UIImage<>(scene, size, size,
                "sprites/main menu background outline buffer.png");

        addChild(outlineBuffer2);
        outlineBuffer2.setZ(.9f);
        centerUIObject(outlineBuffer2, 0);
        outlineBuffer2.setY(outlineBuffer2.getY() - size);

        UIImage<MainMenuScene<CrashLandingGame>> stars = new UIImage<>(scene, size, size,
                "sprites/main menu background stars.png");

        addChild(stars);
        stars.setZ(.02f);
        centerUIObject(stars, 1);

        dust1_1 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 1.png");

        addChild(dust1_1);
        dust1_1.setZ(.10f);
        centerUIObject(dust1_1, 1);

        dust1_2 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 1.png");

        addChild(dust1_2);
        dust1_2.setZ(.10f);
        centerUIObject(dust1_2, 1);
        dust1_2.setY(dust1_2.getY() + size);

        dust2_1 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 2.png");

        addChild(dust2_1);
        dust2_1.setZ(.04f);
        centerUIObject(dust2_1, 1);

        dust2_2 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 2.png");

        addChild(dust2_2);
        dust2_2.setZ(.04f);
        centerUIObject(dust2_2, 1);
        dust2_2.setY(dust2_2.getY() + size);

        dust3_1 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 3.png");

        addChild(dust3_1);
        dust3_1.setZ(.05f);
        centerUIObject(dust3_1, 1);

        dust3_2 = new UIImage<>(scene, size, size,
                "sprites/main menu background dust 3.png");

        addChild(dust3_2);
        dust3_2.setZ(.05f);
        centerUIObject(dust3_2, 1);
        dust3_2.setY(dust3_2.getY() + size);

        planet = new UIImage<>(scene, size, size,
                "sprites/main menu background planet 2.png");

        addChild(planet);
        planet.setZ(.025f);
        centerUIObject(planet, .8f);

        ship = new UIImage<>(scene, 100, 100,
                "sprites/main menu background ship.png");

        ship.setSpriteAnimation(new SpriteAnimationState(ship, scene.getEngine().getAnimation("mainmenuship")));

        addChild(ship);
        ship.setZ(.08f);
        centerUIObject(ship, .2f);
    }

    @Override
    public void onUpdate(float deltaTime) {
        moveBackground(dust1_1, deltaTime * 0.10752352f);
        moveBackground(dust1_2, deltaTime * 0.10752352f);
        moveBackground(dust2_1, deltaTime * 0.05552352f);
        moveBackground(dust2_2, deltaTime * 0.05552352f);
        moveBackground(dust3_1, deltaTime * 0.02552352f);
        moveBackground(dust3_2, deltaTime * 0.02552352f);
        moveBackground(planet, deltaTime * 0.00052352f);
    }

    private void centerUIObject(UIImage<?> object, float randomize) {
        object.setX((xOffset - object.getWidth()) / 2f);
        object.setY((yOffset - object.getHeight()) / 2f);

        object.setInternalTextureXOffset((random.nextFloat() - .5f) * randomize);
        object.setInternalTextureYOffset((random.nextFloat() - .5f) * randomize);
    }

    private void moveBackground(UIImage<?> object, float speed) {
        object.setY(object.getY() - speed);

        if (object.getY() < max - size) {
            object.setY(object.getY() + size * 2f);

            object.setInternalTextureXOffset(random.nextFloat());
        }
    }
}
