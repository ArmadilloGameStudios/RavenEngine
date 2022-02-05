package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.idmap.IDMapGameObject;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;

public class Map extends IDMapGameObject<MapScene, Region> {

    public Map(MapScene scene, GameData data) {
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
