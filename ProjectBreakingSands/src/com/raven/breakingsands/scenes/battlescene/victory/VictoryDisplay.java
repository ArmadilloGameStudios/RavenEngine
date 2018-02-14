package com.raven.breakingsands.scenes.battlescene.victory;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.Character;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;
import com.raven.breakingsands.scenes.hud.HUDCenterContainer;
import com.raven.breakingsands.scenes.missionselectscene.MissionSelectScene;
import com.raven.engine.util.Vector4f;
import com.raven.engine.worldobject.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class VictoryDisplay extends HUDCenterContainer<BattleScene> {

    private Vector4f color = new Vector4f(0, 0, 0, 0);
    private HUDButton<BattleScene, VictoryDisplay> victoryBtn;

    public VictoryDisplay(BattleScene scene) {
        super(scene);

        victoryBtn = new HUDButton<BattleScene, VictoryDisplay>(scene, "Victory!") {
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
//        addChild(victoryBtn);


        HUDLabel<BattleScene, VictoryDisplay> nameLabel =
                new HUDLabel<>(getScene(), "Name", 200, 70);
        addChild(nameLabel);

        HUDLabel<BattleScene, VictoryDisplay> levelLabel =
                new HUDLabel<>(getScene(), "Level", 200, 70);

        addChild(levelLabel);

        HUDLabel<BattleScene, VictoryDisplay> detailsLabel =
                new HUDLabel<>(getScene(), "Details", 200, 146);
        addChild(detailsLabel);

        nameLabel.setYOffset(0);
        levelLabel.setYOffset(0);
        detailsLabel.setYOffset(0);

        nameLabel.setYOffset(76f);
        nameLabel.setXOffset(-103f);

        levelLabel.setXOffset(-103f);

        detailsLabel.setYOffset(76f / 2f);
        detailsLabel.setXOffset(103f);

    }

    @Override
    public float getYOffset() {
        return super.getYOffset() + .5f;
    }

    @Override
    protected void onSetVisibility(boolean visibility) {

        List<String> names = getScene().getCharacterToLevel().stream().map(Character::getName).collect(Collectors.toList());
        String text = String.join(", ", names);

        victoryBtn.setText(text);
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
