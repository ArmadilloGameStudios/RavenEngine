package com.armadillogamestudios.mouseepic.scenes.worldscene;

import com.armadillogamestudios.mouseepic.scenes.worldscene.terrain.Terrain;
import com.raven.engine2d.database.GameData;
import com.raven.engine2d.scene.Layer;
import com.raven.engine2d.worldobject.WorldObject;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class WorldMap extends WorldObject<WorldScene, Terrain, WorldObject> {

    private Terrain[] map;
    private int size;

    public WorldMap(WorldScene scene, int size, GameData[] gameDataMap) {
        super(scene);

        this.size = size;
        map = new Terrain[size * size];

        AtomicInteger i = new AtomicInteger(0);
        Arrays.stream(gameDataMap).forEach(gd -> {
            int x = i.get() / size;
            int y = i.get() % size;

            if (gd != null) {
                map[i.get()] = new Terrain(scene, gd, x, y);
                addChild(map[i.get()]);
            }

            i.incrementAndGet();
        });

    }

    public Terrain getTerrainAt(float x, float y) {
        int ix = (int) x;
        int iy = (int) y;

        if (ix >= 0 && ix < size && iy >= 0 && iy < size)
            return map[ix * size + iy];
        else
            return null;
    }

    @Override
    public Layer.Destination getDestination() {
        return Layer.Destination.Terrain;
    }

    @Override
    public float getZ() {
        return 0;
    }

    public int getSize() {
        return size;
    }
}
