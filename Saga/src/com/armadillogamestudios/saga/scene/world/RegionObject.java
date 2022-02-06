package com.armadillogamestudios.saga.scene.world;

import com.armadillogamestudios.engine2d.idmap.IDMapChild;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.saga.data.world.RegionData;

public class RegionObject extends IDMapChild<WorldScene> {

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
        getMapGameObject().getScene().setRegion(this);
    }

    public Vector2i getCenter() {
        return regionData.getCenter();
    }

    public RegionData getData() {
        return regionData;
    }
}
