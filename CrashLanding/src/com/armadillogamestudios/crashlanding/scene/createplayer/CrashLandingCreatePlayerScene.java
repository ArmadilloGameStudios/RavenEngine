package com.armadillogamestudios.crashlanding.scene.createplayer;

import com.armadillogamestudios.crashlanding.CrashLandingGame;
import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteAnimationState;
import com.armadillogamestudios.engine2d.ui.UIImage;
import com.armadillogamestudios.engine2d.ui.UILabel;
import com.armadillogamestudios.engine2d.ui.UIText;
import com.armadillogamestudios.engine2d.ui.container.UIContainer;
import com.armadillogamestudios.engine2d.util.math.Vector3f;
import com.armadillogamestudios.engine2d.worldobject.Highlight;
import com.armadillogamestudios.storyteller.gameengine.scene.CreatePlayerScene;
import com.armadillogamestudios.storyteller.gameengine.scene.MainMenuScene;

public class CrashLandingCreatePlayerScene extends CreatePlayerScene<CrashLandingGame> {

    public CrashLandingCreatePlayerScene(CrashLandingGame game) {
        super(game);

        addKeyboardHandler(this);
    }

    @Override
    protected void loadUI() {

        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        UIImage<CrashLandingCreatePlayerScene> background = new UIImage<>(this,
                GameProperties.getWidth(), GameProperties.getHeight(),
                "creator background.png");

        addChild(background);
        background.setZ(.1f);

        // title

        UILabel<CrashLandingCreatePlayerScene> title = new UILabel<>(this,
                "Who are you", 100, 100);

        title.setX(10);
        title.setY(245);
        title.setZ(.2f);
        title.setHighlight(new Highlight(0f, 0f, 0f, 1f));
        title.load();
    }

    @Override
    public void updateUI(float deltaTime) {

    }
}
