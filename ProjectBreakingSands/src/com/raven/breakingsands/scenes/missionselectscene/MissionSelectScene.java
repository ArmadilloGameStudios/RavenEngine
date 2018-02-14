package com.raven.breakingsands.scenes.missionselectscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.Character;
import com.raven.breakingsands.mission.Mission;
import com.raven.breakingsands.scenes.battlescene.pawn.Pawn;
import com.raven.breakingsands.scenes.mainmenuscene.DisplayPawn;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Camera;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MissionSelectScene extends Scene<BreakingSandsGame> {

    private HUDCharacterSelect characterSelect;
    private int characterIndex = 0;
    private float zoom = -4f;

    private GlobalDirectionalLight sunLight;
    private List<DisplayPawn> pawns = new ArrayList<>();

    public MissionSelectScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<ModelData> getSceneModels() {
        List<ModelData> modelDataList = new ArrayList<>();

        modelDataList.addAll(DisplayPawn.getModelData());

        return modelDataList;
    }

    private Vector3f tempVec = new Vector3f();
    @Override
    public void onEnterScene() {
        // Camera
        Camera camera = getCamera();
        camera.setInteractable(false);
        camera.setZoomMax(zoom);
        camera.setZoomMin(zoom);
        camera.setZoom(zoom, false);
        camera.setYRotation(40f);
        camera.setHeight(-1);
        camera.setNear(.01f);
        camera.setFar(10f);

        // Light
        sunLight = new GlobalDirectionalLight();
        sunLight.origin = new Vector3f(0, 1, 0);
        sunLight.color = new Vector3f(1, 1, 1);
        sunLight.intensity = 1f;
        sunLight.shadowTransparency = .2f;
        sunLight.size = 5f;
        sunLight.height = 2f;

        Vector3f dir = new Vector3f(1, 5, 2);
        sunLight.setDirection(dir.normalize(tempVec));

        addLight(sunLight);

        // Pawn
        float offset = (getGame().getCharacters().size() - 1) * -1.5f;
        for (Character character : getGame().getCharacters()) {
            DisplayPawn pawn = new DisplayPawn(this);

            pawn.setX(offset);
            offset += 3;

            pawns.add(pawn);
            getLayerDetails().addChild(pawn);
        }

        // Background
        setBackgroundColor(new Vector3f(0f,0f,0f));

        // HUD Mission
        HUDMissionSelect hudMissionSelect = new HUDMissionSelect(this);

        List<Mission> missions = getGame().getMissions();

        missions.add(new Mission("The Head of the Snake"));

        hudMissionSelect.addMissions(missions);
        hudMissionSelect.pack();

        this.getLayerHUD().addChild(hudMissionSelect);

        // HUD Bottom - character
        characterSelect = new HUDCharacterSelect(this);
        characterSelect.pack();
        this.getLayerHUD().addChild(characterSelect);

        //
        selectCharacter(characterIndex);
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

    private void selectCharacter(int index) {
        Character c = getGame().getCharacters().get(index);
        DisplayPawn pawn = pawns.get(index);

        Camera camera = getCamera();
        camera.setPosition(pawn.getX(), pawn.getZ());


        characterSelect.setText(characterText(c));
    }

    private String characterText(Character c) {
        return c.getTitle() + " " + c.getName();
    }

    public void selectCharacterLeft() {
        characterIndex--;

        if (characterIndex < 0) {
            characterIndex = getGame().getCharacters().size() - 1;
        }

        selectCharacter(characterIndex);
    }

    public void selectCharacterRight() {
        characterIndex++;

        if (characterIndex > getGame().getCharacters().size() - 1) {
            characterIndex = 0;
        }

        selectCharacter(characterIndex);
    }

}
