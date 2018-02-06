package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.hud.HUDCenterContainer;
import com.raven.breakingsands.scenes.mainmenuscene.DisplayPawn;
import com.raven.breakingsands.scenes.mainmenuscene.Terrain;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Scene;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MissionSelectScene extends Scene<BreakingSandsGame> {

    public MissionSelectScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<ModelData> getSceneModels() {
        List<ModelData> modelDataList = new ArrayList<>();

        return modelDataList;
    }

    @Override
    public void onEnterScene() {
        // Background
        setBackgroundColor(new Vector3f(0f,0f,0f));

        // HUB
        HUDMissionSelect hudMissionSelect = new HUDMissionSelect(this);

        List<Mission> missions = new ArrayList<>();

        missions.add(new Mission("The Head of the Snake"));

        hudMissionSelect.addMissions(missions);

        this.getLayerHUD().addChild(hudMissionSelect);
    }

    @Override
    public void onExitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    @Override
    public void inputKey(int key, int action, int mods) {

    }
}
