package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.database.GameDatabase;
import com.raven.engine2d.graphics2d.sprite.SpriteSheet;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Terrain extends WorldObject<WorldScene, WorldScene, WorldObject> {

    public static List<SpriteSheet> getSpriteSheets(WorldScene scene) {
        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : GameDatabase.all("terrain")) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    private List<TerrainTrigger> triggerList = new ArrayList<>();
    private GameData gameData;
    private boolean passable;

    public Terrain(WorldScene scene, GameData data, int x, int y) {
        super(scene, data);

        loadGameData(data);

        setX(x);
        setY(y);
    }

    private void loadGameData(GameData data) {
        gameData = data;

        data.ifHas("idle_animations", i -> {
            String idle = i.asList().getRandom(getScene().getRandom()).asString();

            getAnimationState().setIdleAction(idle);
            getAnimationState().setActionIdle();
        });

        passable = true;
        data.ifHas("passable", p -> passable = p.asBoolean(), () -> passable = true);

        triggerList.clear();
        data.ifHas("triggers", d -> d.asList().forEach(t -> triggerList.add(new TerrainTrigger(t))));
    }

    public void replace(GameData gameData) {

        if (gameData.has("sprite")) {
            setSpriteSheet(gameData.getString("sprite"));

            if (gameData.has("animation")) {
                String animationName = gameData.getString("animation");
                setAnimation(animationName);
                getAnimationState().setIdleAction("idle");
            }
        }

        loadGameData(gameData);
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

    public void trigger(String effect) {
        triggerList.stream()
                .filter(t -> t.effect.equals(effect))
                .collect(Collectors.toList())
                .forEach(t -> {
                    if (t.replaceTerrain != null) {
                        GameDatabase.all("terrain").stream()
                                .filter(gd -> gd.getString("name").equals(t.replaceTerrain))
                                .findFirst()
                                .ifPresent(this::replace);
                    }
                });
    }
}
