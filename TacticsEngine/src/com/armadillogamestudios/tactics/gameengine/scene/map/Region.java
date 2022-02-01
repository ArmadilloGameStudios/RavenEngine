package com.armadillogamestudios.tactics.gameengine.scene.map;

import com.armadillogamestudios.engine2d.database.GameData;
import com.armadillogamestudios.engine2d.input.MouseClickHandler;
import com.armadillogamestudios.engine2d.util.math.Vector2f;
import com.armadillogamestudios.engine2d.util.math.Vector2i;
import com.armadillogamestudios.tactics.gameengine.game.gameobject.MapChild;
import org.lwjgl.system.CallbackI;

public class Region extends MapChild {

    private final World<?, ?> world;
    private int id;
    private String name;
    private Vector2i center = new Vector2i();

    public Region(World<?, ?> world, GameData gameData) {
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
