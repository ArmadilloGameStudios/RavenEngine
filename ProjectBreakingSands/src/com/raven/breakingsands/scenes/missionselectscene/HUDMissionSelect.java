package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.hud.HUDLeftContainer;
import com.raven.engine.util.math.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class HUDMissionSelect extends HUDLeftContainer<MissionSelectScene> {

    private Vector4f color = new Vector4f(.25f, .25f, .25f, .5f);
    private List<Mission> missions = new ArrayList<>();

    public HUDMissionSelect(MissionSelectScene scene) {
        super(scene);
    }

    public void addMissions(List<Mission> missions) {
        this.missions.addAll(missions);

        for (Mission mission : missions) {
            HUDMission hm = new HUDMission(getScene(), mission);
            addChild(hm);
            hm.updateTexture();
        }
    }

    @Override
    public Vector4f getColor() {
        return color;
    }
}
