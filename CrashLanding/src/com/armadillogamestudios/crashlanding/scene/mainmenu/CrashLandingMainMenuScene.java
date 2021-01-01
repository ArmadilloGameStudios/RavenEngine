package com.armadillogamestudios.crashlanding.scene.mainmenu;

import com.armadillogamestudios.crashlanding.CrashLandingGame;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.storyteller.gameengine.scene.mainmenu.MainMenuScene;

import java.util.Random;

public class CrashLandingMainMenuScene extends MainMenuScene<CrashLandingGame> {

    private final float xOffset = (float) GameProperties.getWidth();
    private final float yOffset = (float) GameProperties.getHeight();

    private final Random random = new Random();

    private final int size = 300;
    private final float max = ((yOffset - size) / 2f);

    private UIImage<CrashLandingMainMenuScene>
            dust1_1, dust1_2, dust2_1, dust2_2, dust3_1, dust3_2,
            planet, ship;

    public CrashLandingMainMenuScene(CrashLandingGame game) {
        super(game);
    }

    @Override
    protected void loadUI() {

        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        UIImage<MainMenuScene<CrashLandingGame>> background = new UIImage<>(this,
                GameProperties.getWidth(), GameProperties.getHeight(),
                "sprites/main menu background.png");

        addChild(background);
        background.setZ(.1f);

        // Window
        UIImage<MainMenuScene<CrashLandingGame>> stars = new UIImage<>(this, size, size,
                "sprites/main menu background stars.png");

        addChild(stars);
        stars.setZ(.02f);
        centerSpaceUIObject(stars, 1);

        dust1_1 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 1.png");

        addChild(dust1_1);
        dust1_1.setZ(.03f);
        centerSpaceUIObject(dust1_1, 1);

        dust1_2 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 1.png");

        addChild(dust1_2);
        dust1_2.setZ(.03f);
        centerSpaceUIObject(dust1_2, 1);
        dust1_2.setY(dust1_2.getY() + size);

        dust2_1 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 2.png");

        addChild(dust2_1);
        dust2_1.setZ(.04f);
        centerSpaceUIObject(dust2_1, 1);

        dust2_2 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 2.png");

        addChild(dust2_2);
        dust2_2.setZ(.04f);
        centerSpaceUIObject(dust2_2, 1);
        dust2_2.setY(dust2_2.getY() + size);

        dust3_1 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 3.png");

        addChild(dust3_1);
        dust3_1.setZ(.05f);
        centerSpaceUIObject(dust3_1, 1);

        dust3_2 = new UIImage<>(this, size, size,
                "sprites/main menu background dust 3.png");

        addChild(dust3_2);
        dust3_2.setZ(.05f);
        centerSpaceUIObject(dust3_2, 1);
        dust3_2.setY(dust3_2.getY() + size);

        planet = new UIImage<>(this, size, size,
                "sprites/main menu background planet 2.png");

        addChild(planet);
        planet.setZ(.025f);
        centerSpaceUIObject(planet, .8f);

        ship = new UIImage<>(this, 100, 100,
                "sprites/main menu background ship.png");

        ship.setSpriteAnimation(new SpriteAnimationState(ship, this.getEngine().getAnimation("mainmenuship")));

        addChild(ship);
        ship.setZ(.08f);
        centerSpaceUIObject(ship, .2f);

        // Buttons
        UIContainer<CrashLandingMainMenuScene> buttons = new UIContainer<>(this, UIContainer.Location.BOTTOM_RIGHT, UIContainer.Layout.VERTICAL);

        if (this.isLoadGame()) {
            LoadGameButton loadBtn = new LoadGameButton(this);
            loadBtn.load();
            loadBtn.setZ(.2f);
            buttons.addChild(loadBtn);
        }
        
        NewGameButton newGameBtn = new NewGameButton(this);
        newGameBtn.setZ(.2f);
        newGameBtn.load();
        buttons.addChild(newGameBtn);

        SettingsButton settingsButton = new SettingsButton(this);
        settingsButton.setZ(.2f);
        settingsButton.load();
        buttons.addChild(settingsButton);

        CreditsButton creditsButton = new CreditsButton(this);
        creditsButton.setZ(.2f);
        creditsButton.load();
        buttons.addChild(creditsButton);

        ExitButton exitBtn = new ExitButton(this);
        exitBtn.setZ(.2f);
        exitBtn.load();
        buttons.addChild(exitBtn);

        addChild(buttons);
        buttons.setXOffset(-10);
        buttons.pack();
    }

    @Override
    public void onExitScene() {

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

    private void centerSpaceUIObject(UIImage<?> object, float randomize) {
        object.setX((xOffset - object.getWidth()) / 2f);
        object.setX(object.getX() - 140);
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
