package com.raven.breakingsands.scenes.battlescene.victory;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.breakingsands.scenes.hud.HUDCenterContainer;
import com.raven.breakingsands.scenes.missionselectscene.MissionSelectScene;
import com.raven.engine.util.Vector4f;

public class VictoryDisplay extends HUDCenterContainer<BattleScene> {

    private Vector4f color = new Vector4f(.25f, .25f, .25f, .5f);
    private HUDButton<BattleScene, VictoryDisplay> victory;

    public VictoryDisplay(BattleScene scene) {
        super(scene);

        victory = new HUDButton<BattleScene, VictoryDisplay>(scene, "Victory!") {
            @Override
            public float getHeight() {
                return 75;
            }

            @Override
            public float getWidth() {
                return 200;
            }

            @Override
            public void handleMouseClick() {
                victoryClicked();
            }
        };

        addChild(victory);
    }

    private void victoryClicked() {
        BreakingSandsGame game = getScene().getGame();

        game.resolveActiveMission(true);
        game.prepTransitionScene(new MissionSelectScene(game));
    }

    @Override
    public Vector4f getColor() {
        return color;
    }
}
