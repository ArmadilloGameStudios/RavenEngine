package com.armadillogamestudios.breakingsands.character;

import com.armadillogamestudios.breakingsands.ZLayer;
import com.armadillogamestudios.breakingsands.scenes.battlescene.BattleScene;
import com.armadillogamestudios.breakingsands.scenes.battlescene.map.Terrain;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.database.GameDatabase;
import com.armadillogamestudios.engine2d.graphics2d.sprite.SpriteSheet;
import com.armadillogamestudios.engine2d.scene.Layer;
import com.armadillogamestudios.engine2d.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Effect extends WorldObject<BattleScene, Terrain, WorldObject> {

    public static List<SpriteSheet> getSpriteSheets(BattleScene scene) {

        List<SpriteSheet> data = new ArrayList<>();

        for (GameData gameData : GameDatabase.all("effect")) {
            data.add(scene.getEngine().getSpriteSheet(gameData.getString("sprite")));
        }

        return data;
    }

    private String name;

    public Effect(BattleScene scene, GameData gameData) {
        super(scene, gameData);

        this.setVisibility(false);

        name = gameData.getString("name");
//        System.out.println(gameData);
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Effects;
    }

    @Override
    public float getZ() {
        return ZLayer.EFFECT.getValue();
    }
}
