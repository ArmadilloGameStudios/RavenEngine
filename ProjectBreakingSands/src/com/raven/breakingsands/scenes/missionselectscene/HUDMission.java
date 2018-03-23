package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.breakingsands.scenes.hud.HUDButton;

import java.awt.*;

public class HUDMission extends HUDButton<MissionSelectScene, HUDMissionSelect> {

    private Mission mission;

    public HUDMission(MissionSelectScene scene, Mission mission) {
        super(scene, mission.getName());

        this.mission = mission;

        setFont(new Font( "SansSerif", Font.PLAIN, 14));
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

        game.setActiveMission(mission);
        game.prepTransitionScene(new BattleScene(game));
    }
}
