package com.raven.breakingsands.scenes.mainmenuscene;

import com.raven.breakingsands.BreakingSandsGame;
import com.raven.breakingsands.scenes.hud.HUDBottomContainer;
import com.raven.engine.graphics3d.ModelData;
import com.raven.engine.scene.Camera;
import com.raven.engine.scene.Scene;
import com.raven.engine.scene.light.GlobalDirectionalLight;
import com.raven.engine.util.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScene extends Scene<BreakingSandsGame> {

    private float zoom = -4f;
    private GlobalDirectionalLight sunLight;

    private DisplayPawn pawn;

    public MainMenuScene(BreakingSandsGame game) {
        super(game);
    }

    @Override
    public List<ModelData> getSceneModels() {
        List<ModelData> modelDataList = new ArrayList<>();

        modelDataList.addAll(Terrain.getModelData());
        modelDataList.addAll(DisplayPawn.getModelData());

        return modelDataList;
    }

    @Override
    public void enterScene() {
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
        sunLight.setDirection(dir.normalize());

        addLight(sunLight);

        // Pawn
        pawn = new DisplayPawn(this);
        getLayerDetails().addChild(pawn);

        // Background
        setBackgroundColor(new Vector3f(0f,0f,0f));

        // HUD
        HUDBottomContainer container = new HUDBottomContainer(this);
        getLayerHUD().addChild(container);

        NewGameButton newGameBtn = new NewGameButton(this);
        container.addChild(newGameBtn);

        ExitButton exitBtn = new ExitButton(this);
        container.addChild(exitBtn);
    }

    @Override
    public void exitScene() {

    }

    @Override
    public void onUpdate(float deltaTime) {
        pawn.setRotation(pawn.getRotation() + deltaTime * .01f);
    }
}
