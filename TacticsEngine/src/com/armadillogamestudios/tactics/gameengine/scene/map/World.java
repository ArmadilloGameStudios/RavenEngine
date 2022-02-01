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

    @Override
    public Region constructChild(GameData gameData) {
        return new Region(this, gameData);
    }

    public void focus(Vector2i center) {
        Vector2f offset = this.getScene().getWorldOffset();

        offset.x = -center.x + (GameProperties.getWidth() / 2);
        offset.y = -center.y + (GameProperties.getHeight() / 2);
    }

    public void focus(int id) {
        focus(getChildByID(id).getCenter());
    }
}
