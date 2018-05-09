package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.UIBottomContainer;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Camera;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainMenuScene extends Scene<BreakingSandsGame> {

    private DisplayPawn pawn;

    public MainMenuScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<SpriteSheet> getSpriteSheets() {
        List<SpriteSheet> modelDataList = new ArrayList<>();

        modelDataList.addAll(DisplayPawn.getSpriteSheets());
        modelDataList.add(NewGameButton.getSpriteSheet());

        return modelDataList;
    }

    private Vector3f tempVec = new Vector3f();
    @Override
    public void onEnterScene() {
        // Pawn
        pawn = new DisplayPawn(this);
        getLayerDetails().addChild(pawn);

        // Background
        setBackgroundColor(new Vector3f(.5f,.5f,.5f));

        // World Offset

        Vector2f wo = getWorldOffset();

        wo.x = GameProperties.getScreenWidth() / 2f;
        wo.y = GameProperties.getScreenHeight() / 2f;

        // UI
        UIBottomContainer container = new UIBottomContainer(this);
        getLayerUI().addChild(container);

        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

//        if (charPath.toFile().list().length > 0) {
//            ContinueButton continueBtn = new ContinueButton(this);
//            container.addChild(continueBtn);
//            continueBtn.updateTexture();
//        }

        NewGameButton newGameBtn = new NewGameButton(this);
        container.addChild(newGameBtn);
//        newGameBtn.updateTexture();

//        ExitButton exitBtn = new ExitButton(this);
//        container.addChild(exitBtn);
//        exitBtn.updateTexture();

        container.pack();
    }

    @Override
    public void onExitScene() {
        getGame().saveGame();
    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void inputKey(int key, int action, int mods) {
        if (GLFW.GLFW_KEY_ESCAPE == key && GLFW.GLFW_PRESS == action) {
            getGame().exit();
        }
    }
}
