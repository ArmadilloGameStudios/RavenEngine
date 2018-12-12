package com.armadillogamestudios.mouseepic.scenes.worldscene.terrain;

import com.armadillogamestudios.mouseepic.scenes.worldscene.WorldScene;
import com.raven.engine2d.database.GameData;

public class Coast extends Terrain {
    public enum Side {
        LEFT, RIGHT, TOP, BOTTOM;
    }

    private static GameData getGameData(Side side) {
        switch (side) {
            case LEFT:
                return Terrain.coast_left;
            case RIGHT:
                return Terrain.coast_right;
            case BOTTOM:
                return Terrain.coast_bottom;
            case TOP:
                return Terrain.coast_top;
        }

        return null;
    }

    public Coast(WorldScene scene, int x, int y, Side side) {
        super(scene, getGameData(side), x, y);
    }
}
