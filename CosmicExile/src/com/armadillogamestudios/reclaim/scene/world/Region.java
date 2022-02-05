package com.armadillogamestudios.reclaim.scene.world;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.idmap.IDMapChild;
import com.armadillogamestudios.engine2d.util.math.Vector2i;

public class Region extends IDMapChild {

    private final ReclaimWorld world;
    private final int id;
    private final String name;
    private final Vector2i center = new Vector2i();

    public Region(ReclaimWorld world, GameData gameData) {
        this.world = world;

        id = gameData.getInteger("id");
        name = gameData.getString("name");

        center.x = gameData.getData("center").getInteger("x");
        center.y = gameData.getData("center").getInteger("y");
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void handleMouseClick() {
        System.out.println(name);
        world.focus(center);
    }

    public Vector2i getCenter() {
        return center;
    }
}
