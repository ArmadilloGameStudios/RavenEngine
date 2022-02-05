package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.tactics.gameengine.game.TacticsGame;
import com.armadillogamestudios.tactics.gameengine.game.gameobject.MapGameObject;

public class World<S extends MapScene<S, G>, G extends TacticsGame<G>> extends MapGameObject<S, Region> {

    public World(S scene, GameData data) {
        super(scene, data);
    }
}
