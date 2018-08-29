package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.character.Effect;
import com.raven.breakingsands.character.Weapon;
import com.raven.breakingsands.scenes.battlescene.decal.Wall;
import com.raven.breakingsands.scenes.battlescene.map.Terrain;
import com.raven.breakingsands.scenes.battlescene.menu.MenuButton;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.hud.UIBottomLeftContainer;
import com.raven.engine2d.GameProperties;
import com.raven.engine2d.graphics2d.shader.ShaderTexture;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Scene;
import com.raven.engine2d.ui.UIButton;
import com.raven.engine2d.util.math.Vector2f;
import com.raven.engine2d.util.math.Vector3f;
import org.lwjgl.glfw.GLFW;

import java.io.File;
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
    public void loadShaderTextures() {
        List<ShaderTexture> textures = getShaderTextures();

        textures.addAll(DisplayPawn.getSpriteSheets(this));
    }

    private Vector3f tempVec = new Vector3f();

    @Override
    public void onEnterScene() {
        // Pawn
        pawn = new DisplayPawn(this);
        addChild(pawn);

        // Background
        setBackgroundColor(new Vector3f(0, 0, 0));

        // World Offset
        Vector2f wo = getWorldOffset();

        wo.x = GameProperties.getScreenWidth() / GameProperties.getScaling() - 32;
        wo.y = GameProperties.getScreenHeight() / GameProperties.getScaling();

        // UI
        UIBottomLeftContainer<MainMenuScene> container = new UIBottomLeftContainer<>(this);
        addChild(container);

        Path charPath = Paths.get(getGame().getMainDirectory(), "save");

        File sFile = charPath.toFile();
        if (sFile.list() == null) {
            sFile.mkdir();
        }

        if (sFile.list().length > 0) {
            ContinueButton continueBtn = new ContinueButton(this);
            continueBtn.load();
            container.addChild(continueBtn);
        }

        NewGameButton newGameBtn = new NewGameButton(this);
        newGameBtn.load();
        container.addChild(newGameBtn);

        ExitButton exitBtn = new ExitButton(this);
        exitBtn.load();
        container.addChild(exitBtn);

        container.pack();

        // Music
//        getGame().playSong("Chapter 50.wav");
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
