package com.raven.breakingsands.scenes.battlescene.map;

import com.raven.breakingsands.scenes.battlescene.BattleScene;
import com.raven.engine.worldobject.WorldObject;

import java.util.ArrayList;
import java.util.List;

public class Structure extends WorldObject<BattleScene, Map, WorldObject> {

    private int width = 3, height = 3;
    private int x, y;

    private List<Terrain> terrainList = new ArrayList<>();

    public Structure(BattleScene scene, int x, int y) {
        super(scene);

        this.x = x;
        this.y = y;

        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                Terrain t = new Terrain(scene, this, "sand", w, h);
                addChild(t);
                terrainList.add(t);
            }
        }


        this.setX(x * 2);
        this.setZ(y * 2);
    }

    public List<Terrain> getTerrainList() {
        return terrainList;
    }

    public int getMapX() {
        return x;
    }

    public int getMapY() {
        return y;
    }
}
