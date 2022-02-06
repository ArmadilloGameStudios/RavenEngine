package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.GameProperties;
import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.idmap.IDMapGameObject;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.saga.data.SagaGameData;
import com.armadillogamestudios.saga.data.world.RegionData;
import com.armadillogamestudios.saga.data.world.WorldData;

public class WorldObject extends IDMapGameObject<WorldScene, RegionObject> {

    private final WorldData worldData;

    public WorldObject(WorldScene scene, WorldData data) {
        super(scene, data.toGameData());

        worldData = data;
    }

    @Override
    public RegionObject constructChild(GameData gameData) {
       RegionData regionData = SagaGameData.getRegionDataByID(gameData.getInteger("id"));

        return new RegionObject(this, regionData);
    }

    public void focus(Vector2i center) {
        Vector2f offset = this.getScene().getWorldOffset();

        offset.x = -center.x + (GameProperties.getWidth() / 2);
        offset.y = -center.y + (GameProperties.getHeight() / 2);
    }

    public void focus(int id) {
        RegionObject regionObject = getChildByID(id);
        regionObject.handleMouseClick();
    }
}
