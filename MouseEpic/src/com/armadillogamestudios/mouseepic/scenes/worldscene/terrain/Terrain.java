package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Terrain extends WorldObject<WorldScene, WorldScene, WorldObject> {

    public static List<SpriteSheet> getSpriteSheets(WorldScene scene) {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : GameDatabase.all("terrain")) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    private GameData gameData;
    private boolean passable;

    public Terrain(WorldScene scene, GameData data, int x, int y) {
        super(scene, data);

        gameData = data;

        data.ifHas("idle_animations", i -> {
            String idle = i.asList().getRandom(getScene().getRandom()).asString();

            getAnimationState().setIdleAction(idle);
            getAnimationState().setActionIdle();
        });

        data.ifHas("passable", p -> passable = p.asBoolean(), () -> passable = true);

        setX(x);
        setY(y);
    }

    public GameData getGameData() {
        return gameData;
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return .1f;
    }

    public boolean isPassable() {
        return passable;
    }
}
