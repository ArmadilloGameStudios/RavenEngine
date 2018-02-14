package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.breakingsands.scenes.missionselectscene.MissionSelectScene;
import com.raven.engine.GameEngine;
import com.raven.engine.scene.Layer;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.*;

import java.awt.*;

public class NewGameButton
        extends HUDButton<MainMenuScene, HUDContainer<MainMenuScene>> {

    public NewGameButton(MainMenuScene scene) {
        super(scene, "New Game");
    }

    @Override
    public float getHeight() {
        return 75f;
    }

    @Override
    public float getWidth() {
        return 200f;
    }

    @Override
    public void handleMouseClick() {
        BreakingSandsGame game = getScene().getGame();

        game.newGame();

        game.prepTransitionScene(new MissionSelectScene(game));
    }
}
