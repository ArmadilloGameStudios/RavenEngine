package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Terrain extends WorldObject<WorldScene, WorldScene, WorldObject> {

    protected static GameData grass, coast_left, coast_right, coast_bottom, coast_top;

    public static List<SpriteSheet> getSpriteSheets(WorldScene scene) {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : GameDatabase.all("terrain")) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    public static void loadData() {
        for (GameData gameData : GameDatabase.all("terrain")) {
            switch (gameData.getString("name").toLowerCase()) {
                case "grass":
                    grass = gameData;
                    break;
                case "grass coast left":
                    coast_left = gameData;
                    break;
                case "grass coast right":
                    coast_right = gameData;
                    break;
                case "grass coast bottom":
                    coast_bottom = gameData;
                    break;
                case "grass coast top":
                    coast_top = gameData;
                    break;
            }
        }
    }

    public Terrain(WorldScene scene, GameData data, int x, int y) {
        super(scene, data);

        data.ifHas("idle_animations", i -> {
            String idle = i.asList().getRandom(getScene().getRandom()).asString();

            System.out.println(idle);

            getAnimationState().setIdleAction(idle);
            getAnimationState().setActionIdle();
        });

        setX(x);
        setY(y);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .1f;
    }
}
