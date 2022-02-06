package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.idmap.IDMapChild;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.saga.data.SagaGameData;
import com.armadillogamestudios.saga.data.world.RegionData;
import com.armadillogamestudios.saga.data.world.TerrainData;

public class RegionObject extends IDMapChild {

    private final WorldObject worldObject;
    private final RegionData regionData;

    public RegionObject(WorldObject worldObject, RegionData regionData) {
        super(worldObject);

        this.worldObject = worldObject;
        this.regionData = regionData;
    }

    @Override
    public int getID() {
        return regionData.getID();
    }

    @Override
    public void handleMouseClick() {
        this.getMapGameObject().getScene();

        worldObject.focus(regionData.getCenter());
    }

    public Vector2i getCenter() {
        return regionData.getCenter();
    }
}
